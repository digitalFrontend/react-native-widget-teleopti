import CallKit
import WidgetKit

@objc(WidgetShareData)
class WidgetShareData: NSObject {
    
    func saveData(transferData: WidgetTransferData){
        do {
            let sharedDefaults = UserDefaults(suiteName: DATA_GROUP);
          
            let encoded = try JSONEncoder().encode(transferData);
            let jsonString = String(data: encoded,
                                    encoding: .utf8)
          
            sharedDefaults?.set(jsonString, forKey: DATA_KEY);
        }catch {
            print(error.localizedDescription)
        }
    }
    
    func loadData() -> WidgetTransferData {
        let transferData = (UserDefaults(suiteName: DATA_GROUP)?.string(forKey: DATA_KEY) ?? nil)
 
        if (transferData != nil ){
            let jsonData = transferData!.data(using: .utf8)!
            let data: WidgetTransferData = try! JSONDecoder().decode(WidgetTransferData.self, from: jsonData)

            return data
        } else {
            let data = WidgetTransferData(data: [])
            
            return data;
        }
    }
    
    @objc
    func setDataList(
        _ dataList: Array<Any>,
        withExtensionId EXTENSION_ID: String,
        withDataGroup DATA_GROUP: String,
        withDataKey DATA_KEY: String,
        withResolver resolve: @escaping RCTPromiseResolveBlock,
        withRejecter reject:  @escaping RCTPromiseRejectBlock
    ) -> Void {
        
        do {
           
            let transferData = WidgetTransferData(data: dataList as! [String])
            saveData(transferData: transferData)
            if #available(iOS 14.0, *) {
                WidgetCenter.shared.reloadTimelines(ofKind: "WidgetTeleopti")
                resolve("true");
            } else {
                // Fallback on earlier versions
            }
    
            
        } catch {
            reject("WIDGET", error.localizedDescription, error);
        }
    }
    

   
   
}
