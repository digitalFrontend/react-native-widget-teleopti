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
            let data = WidgetTransferData(data: [], updateDate: "", hasTeleopti: false)
            
            return data;
        }
    }

    func resetMetrics(){
        do {
            let sharedDefaults = UserDefaults(suiteName: DATA_GROUP);
            sharedDefaults?.set(nil, forKey: METRIC_KEY);
        }catch {
            print(error.localizedDescription)
        }
    }
    @objc
    func setDataList(
        _ dataList: Array<Any>,
        withUpdateDate updateDate: String,
        withHasTeleopti hasTeleopti: Bool,
        withExtensionId EXTENSION_ID: String,
        withDataGroup DATA_GROUP: String,
        withDataKey DATA_KEY: String,
        withResolver resolve: @escaping RCTPromiseResolveBlock,
        withRejecter reject:  @escaping RCTPromiseRejectBlock
    ) -> Void {
        
        do {
           
            let transferData = WidgetTransferData(data: dataList as! [String], updateDate: updateDate, hasTeleopti: hasTeleopti)
            saveData(transferData: transferData)
            if #available(iOS 14.0, *) {
                WidgetCenter.shared.reloadTimelines(ofKind: "WidgetTeleopti")
                resolve("true");
            } else {
                resolve("true");
                // Fallback on earlier versions
            }
        } catch {
            reject("WIDGET", error.localizedDescription, error);
        }
    }
    

    @objc
    func loadMetrics(_ resolve: @escaping RCTPromiseResolveBlock, withRejecter reject: @escaping RCTPromiseRejectBlock) -> Void{
        if #available(iOS 14.0, *) {
            var widgetsList:[WidgetInfoEncodable] = []
            WidgetCenter.shared.getCurrentConfigurations { results in
                let widgets = try? results.get() ?? []
                for i in 0..<(widgets?.count ?? 0) {
                    widgetsList.append(WidgetInfoEncodable(configuration: nil, family: widgets?[i].family.description, kind: widgets?[i].kind))
                }
                do {
                    print("widgetsList", widgetsList)
                    let encoded = try JSONEncoder().encode(widgetsList);
                    let jsonString = String(data: encoded, encoding: .utf8)
                    resolve(jsonString)
                }catch {
                    print(error.localizedDescription)
                
                }
            }
           
         
        } else {
            resolve("[]")
            // Fallback on earlier versions
        }
       
    }
//    @objc
//    func loadMetrics(_ resolve: @escaping RCTPromiseResolveBlock, withRejecter reject: @escaping RCTPromiseRejectBlock) -> Void{
//        if #available(iOS 14.0, *) {
//            WidgetCenter.shared.getCurrentConfigurations { results in
//                guard let widgets = try? results.get() else { return }
//                resolve(widgets)
//            }
//        } else {
//            resolve([])
//            // Fallback on earlier versions
//        }
//
//    }
   
}
