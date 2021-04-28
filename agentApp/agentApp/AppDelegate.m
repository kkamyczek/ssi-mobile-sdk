//
//  AppDelegate.m
//  agentApp
//
//  Created by Krzysztof on 24/03/2021.
//

#import "AppDelegate.h"
#import "Header.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
//    Kotlin_multiplatform_agentIndyWalletHolder *w = [[Kotlin_multiplatform_agentIndyWalletHolder alloc] init];
//    [w openOrCreateWallet];
    ConnectionInitiatorControllerImpl *cici = [[ConnectionInitiatorControllerImpl alloc] init];
    
    Kotlin_multiplatform_agentSsiAgentApiImpl* kmasabi = [[[Kotlin_multiplatform_agentSsiAgentBuilderImpl alloc] withConnectionInitiatorControllerConnectionInitiatorController:cici] build];
    
    [kmasabi doInit];
    
    NSString *ns = @"ws://192.168.0.104:9000/ws?c_i=eyJsYWJlbCI6IlZlcmlmaWVyIiwiaW1hZ2VVcmwiOm51bGwsInNlcnZpY2VFbmRwb2ludCI6IndzOi8vMTkyLjE2OC4wLjEwNDo5MDAwL3dzIiwicm91dGluZ0tleXMiOlsiR295aXM4TG5oQ0JMZmoxdnFlc1U1NUtjb2RIRVQ0b1VucXFLTmZSNEs5c3UiXSwicmVjaXBpZW50S2V5cyI6WyJEcG5hRkFFS2VYS3ZyaEJWSjY1dUJZWEsxbldDaHlFeWpTb1FFVldocFRxTiJdLCJAaWQiOiI1YmE0ZTE1YS03ZWZmLTQ1ODUtOTAzYy02MzhmMTJiYjhkMWYiLCJAdHlwZSI6ImRpZDpzb3Y6QnpDYnNOWWhNcmpIaXFaRFRVQVNIZztzcGVjL2Nvbm5lY3Rpb25zLzEuMC9pbnZpdGF0aW9uIn0=";
    [kmasabi connectUrl:ns];

    return YES;
}

#pragma mark - UISceneSession lifecycle


- (UISceneConfiguration *)application:(UIApplication *)application configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession options:(UISceneConnectionOptions *)options {
    // Called when a new scene session is being created.
    // Use this method to select a configuration to create the new scene with.
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}


- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions {
    // Called when the user discards a scene session.
    // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
    // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
}


@end
