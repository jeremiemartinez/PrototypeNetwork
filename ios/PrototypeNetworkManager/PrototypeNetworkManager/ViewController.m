//
//  ViewController.m
//  PrototypeNetworkManager
//
//  Created by Jeremie MARTINEZ on 24/09/12.
//  Copyright (c) 2012 Jeremie MARTINEZ. All rights reserved.
//

#import "ViewController.h"
#import "Reachability.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.url.text = @"http://www.wallpaper4me.com/images/wallpapers/androidCPU-14756.jpeg";
    self.url.textAlignment = UITextAlignmentCenter;
    [self.progressView setProgress:0.0];
    [[NSNotificationCenter defaultCenter] addObserver: self selector: @selector(reachabilityChanged:) name: kReachabilityChangedNotification object: nil];
    self.wifiReach = [[Reachability reachabilityForLocalWiFi] retain];
	[self.wifiReach startNotifier];
	[self updateInterfaceWithReachability: self.wifiReach];
    self.startTimeSpeed = [[NSDate date] timeIntervalSince1970];
    
    
}

- (void) configureImageView: (UIImageView*) imageView reachability: (Reachability*) curReach
{
    NetworkStatus netStatus = [curReach currentReachabilityStatus];
    switch (netStatus)
    {
        case NotReachable:
        {
            imageView.image = [UIImage imageNamed: @"stop-32.png"] ;
            break;
        }
            
        case ReachableViaWWAN:
        {
            imageView.image = [UIImage imageNamed: @"WWAN5.png"];
            break;
        }
        case ReachableViaWiFi:
        {
            imageView.image = [UIImage imageNamed: @"Airport.png"];
            break;
        }
    }
}

//Called by Reachability whenever status changes.
- (void) reachabilityChanged: (NSNotification* )note
{
	Reachability* curReach = [note object];
	NSParameterAssert([curReach isKindOfClass: [Reachability class]]);
	[self updateInterfaceWithReachability: curReach];
}

- (void) updateInterfaceWithReachability: (Reachability*) curReach
{

    [self configureImageView: self.wifi reachability: curReach];
	
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)startSpeedTest:(id)sender{
    if ([self.startButton.currentTitle isEqualToString:@"Démarrer !"]){
        self.startTimeTotal = [[NSDate date] timeIntervalSince1970];
        self.startTimeSpeed = [[NSDate date] timeIntervalSince1970];
        [self.progressView setProgress:0.0];
        self.request = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:self.url.text]];
        [self.request setDelegate:self];
        self.request.showAccurateProgress = YES;
        [self.request setDownloadProgressDelegate:self];
        [self.request startAsynchronous];
        [self.startButton setTitle:@"Stop !" forState:UIControlStateNormal];
    } else{
        [self.request cancel];
        [self.progressView setProgress:0.0];
        self.request = nil;
        [self.startButton setTitle:@"Démarrer !" forState:UIControlStateNormal];
    }
}

- (void)requestFinished:(ASIHTTPRequest *)request
{
    [self.startButton setTitle:@"Démarrer !" forState:UIControlStateNormal];
    // Use when fetching binary data
    NSData *responseData = [request responseData];
    NSTimeInterval timeDownload = [[NSDate date] timeIntervalSince1970] - self.startTimeTotal;
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Download Done"
                                                    message:[NSString stringWithFormat:@"I just downloaded %d kB in %.2fs, i.e. %.1f kB/s", responseData.length/1000, timeDownload, responseData.length/1000/timeDownload]
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
    [alert release];
    [self.progressView setProgress:0.0];
}


- (void)setProgress:(float)newProgress {
    [self.progressView setProgress:newProgress];
    self.percentage.text = [NSString stringWithFormat:@"%.1f%%", self.progressView.progress*100];
}

- (void)requestFailed:(ASIHTTPRequest *)request
{
    NSError *error = [request error];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                    message:[NSString stringWithFormat:@"Error while downloading %@", [error description]]
                                                   delegate:nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
    [alert release];
    [self.progressView setProgress:0.0];
}

// Called when the request receives some data - bytes is the length of that data
- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes{
    NSTimeInterval time = [[NSDate date] timeIntervalSince1970] - self.startTimeSpeed;
    self.speed.text = [NSString stringWithFormat:@"%.1f kB/s", bytes/1000/time ];
    self.startTimeSpeed = [[NSDate date] timeIntervalSince1970];
}

-(BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationLandscapeLeft) || (interfaceOrientation == UIInterfaceOrientationLandscapeRight);
}


@end
