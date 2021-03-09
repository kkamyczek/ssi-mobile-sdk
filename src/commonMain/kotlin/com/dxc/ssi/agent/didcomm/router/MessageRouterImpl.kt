package com.dxc.ssi.agent.didcomm.router

import com.dxc.ssi.agent.api.Callbacks
import com.dxc.ssi.agent.api.pluggable.LedgerConnector
import com.dxc.ssi.agent.api.pluggable.Transport
import com.dxc.ssi.agent.api.pluggable.wallet.WalletConnector
import com.dxc.ssi.agent.didcomm.processor.didexchange.DidExchangeProcessor
import com.dxc.ssi.agent.didcomm.processor.didexchange.DidExchangeProcessorImpl
import com.dxc.ssi.agent.didcomm.processor.issue.CredIssuerProcessor
import com.dxc.ssi.agent.didcomm.processor.issue.CredIssuerProcessorImpl
import com.dxc.ssi.agent.didcomm.processor.trustping.TrustPingProcessorImpl
import com.dxc.ssi.agent.didcomm.processor.verify.CredVerifierProcessor
import com.dxc.ssi.agent.didcomm.processor.verify.CredVerifierProcessorImpl
import com.dxc.ssi.agent.model.messages.BasicMessageWithTypeOnly
import com.dxc.ssi.agent.model.messages.MessageContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MessageRouterImpl(
    private val walletConnector: WalletConnector,
    private val ledgerConnector: LedgerConnector,
    private val transport: Transport,
    private val callbacks: Callbacks
) :
    MessageRouter {

    override val trustPingProcessor = TrustPingProcessorImpl(walletConnector, transport)
    override val didExchangeProcessor: DidExchangeProcessor = DidExchangeProcessorImpl(
        walletConnector,
        transport,
        trustPingProcessor,
        callbacks.connectionInitiatorController,
        callbacks.connectionResponderController
    )
    private val credIssuerProcessor: CredIssuerProcessor = CredIssuerProcessorImpl(
        walletConnector,
        ledgerConnector,
        transport,
        callbacks.credIssuerController,
        callbacks.credReceiverController
    )
    private val credVerifierProcessor: CredVerifierProcessor = CredVerifierProcessorImpl(walletConnector, transport)

    //TODO: check connection in message context and throw exception if it is not null if it is expected to be non-null
    override fun routeAndProcessMessage(messageContext: MessageContext) {


        val route = determineRoute(messageContext)

        //TODO: think about concurrency here
        when (route) {
            //TODO: add route for forward message
            Route.DidExchange -> didExchangeProcessor.processMessage(messageContext)
            Route.CredIssuer -> credIssuerProcessor.processMessage(messageContext)
            Route.CredVerifier -> credVerifierProcessor.processMessage(messageContext)
        }

    }

    fun determineRoute(messageContext: MessageContext): Route {
        //TODO: unify this extraction of type from message. Currently it is used in sevral places
        val typeAttribute =
            Json { ignoreUnknownKeys = true }.decodeFromString<BasicMessageWithTypeOnly>(messageContext.receivedUnpackedMessage.message).type

        val route = when {
            typeAttribute.contains("/issue-credential/1") -> Route.CredIssuer
            typeAttribute.contains("/connections/1") -> Route.DidExchange
            else -> throw IllegalArgumentException("Unknown message type: $typeAttribute")
        }

        println("Determined route: $route")
        return route

    }

    //TODO: do we need separate root for forward message? is it a protocol or just an envelop?
    enum class Route {
        DidExchange,
        CredIssuer,
        CredVerifier
    }
}