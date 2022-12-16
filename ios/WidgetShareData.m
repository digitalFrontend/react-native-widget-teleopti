#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(WidgetShareData, NSObject)

RCT_EXTERN_METHOD(setDataList: (NSArray*) dataList
                  withUpdateDate:(NSString *) updateDate
                  withHasTeleopti: (BOOL *) hasTeleopti
                  withExtensionId: (NSString *)EXTENSION_ID
                  withDataGroup: (NSString *)DATA_GROUP
                  withDataKey: (NSString *)DATA_KEY
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

@end
