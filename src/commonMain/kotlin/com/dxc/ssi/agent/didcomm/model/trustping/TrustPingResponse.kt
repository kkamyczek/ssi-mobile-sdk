package com.dxc.ssi.agent.didcomm.model.trustping

import com.dxc.ssi.agent.didcomm.model.common.Thread
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
{
  "@type": "https://didcomm.org/trust_ping/1.0/ping",
  "@id": "518be002-de8e-456e-b3d5-8fe472477a86",
  "~timing": {
    "out_time": "2018-12-15 04:29:23Z",
    "expires_time": "2018-12-15 05:29:23Z",
    "delay_milli": 0
  },
  "comment": "Hi. Are you listening?",
  "response_requested": true
}
* */
//TODO: think about some model types instead of just strings
//TODO: support other fields of TrustPing
@Serializable
data class TrustPingResponse(
    @SerialName("@type") val type: String,
    @SerialName("@id") val id: String,
    @SerialName("~thread") val thread: Thread,
)