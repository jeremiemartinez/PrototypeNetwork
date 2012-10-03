//
//  ViewController.h
//  PrototypeNetworkManager
//
//  Created by Jeremie MARTINEZ on 24/09/12.
//  Copyright (c) 2012 Jeremie MARTINEZ. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ASIHTTPRequest.h"
#import "Reachability.h"

@interface ViewController : UIViewController<UIPickerViewDelegate>

@property(nonatomic,retain) IBOutlet UIButton* startButton;
@property(nonatomic,retain) IBOutlet UILabel* url;
@property(nonatomic,retain) IBOutlet UILabel* speed;
@property(nonatomic,retain) IBOutlet UILabel* percentage;
@property(nonatomic,retain) IBOutlet UIImageView* wifi;
@property(nonatomic, retain) IBOutlet UIProgressView* progressView;
@property(nonatomic) NSTimeInterval startTimeTotal;
@property(nonatomic, retain) ASIHTTPRequest* request;
@property(nonatomic, retain) Reachability* wifiReach;
@property(nonatomic) NSTimeInterval startTimeSpeed;
// Called when the request receives some data - bytes is the length of that data
- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes;


@end
