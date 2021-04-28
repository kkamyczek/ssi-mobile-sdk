//
//  SceneDelegate.m
//  agentApp
//
//  Created by Krzysztof on 24/03/2021.
//

#import "kotlin_multiplatform_agent.h"
@interface ConnectionInitiatorControllerImpl: NSObject<Kotlin_multiplatform_agentConnectionInitiatorController>

@end


@implementation ConnectionInitiatorControllerImpl

       
- (nonnull Kotlin_multiplatform_agentCallbackResult *)onCompletedConnection:(nonnull Kotlin_multiplatform_agentConnection_ *)connection {
    NSLog(@"Connection completed");
    Kotlin_multiplatform_agentCallbackResult* ret = [Kotlin_multiplatform_agentCallbackResult alloc];
    Kotlin_multiplatform_agentCallbackResult* w  = [ret initWithCanProceedFurther:TRUE];
    return w;
}

- (nonnull Kotlin_multiplatform_agentCallbackResult *)onInvitationReceivedConnection:(nonnull Kotlin_multiplatform_agentConnection_ *)connection endpoint:(nonnull NSString *)endpoint invitation:(nonnull Kotlin_multiplatform_agentInvitation *)invitation {
    NSLog(@"Invitation");
    Kotlin_multiplatform_agentCallbackResult* ret = [Kotlin_multiplatform_agentCallbackResult alloc];
    Kotlin_multiplatform_agentCallbackResult* w  = [ret initWithCanProceedFurther:TRUE];
    return w;
    
}

- (nonnull Kotlin_multiplatform_agentCallbackResult *)onRequestSentConnection:(nonnull Kotlin_multiplatform_agentConnection_ *)connection request:(nonnull Kotlin_multiplatform_agentConnectionRequest *)request {
    NSLog(@"Send Connection");
    Kotlin_multiplatform_agentCallbackResult* ret = [Kotlin_multiplatform_agentCallbackResult alloc];
    Kotlin_multiplatform_agentCallbackResult* w  = [ret initWithCanProceedFurther:TRUE];
    return w;
    
}

- (nonnull Kotlin_multiplatform_agentCallbackResult *)onResponseReceivedConnection:(nonnull Kotlin_multiplatform_agentConnection_ *)connection response:(nonnull Kotlin_multiplatform_agentConnectionResponse *)response {
    Kotlin_multiplatform_agentCallbackResult* ret = [Kotlin_multiplatform_agentCallbackResult alloc];
    Kotlin_multiplatform_agentCallbackResult* w  = [ret initWithCanProceedFurther:TRUE];
    return w;
    
}

@end
