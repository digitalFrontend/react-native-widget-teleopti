//
//  WidgetTeleopti.swift
//  WidgetTeleopti
//
//  Created by Admin on 17.08.2022.
//  Copyright © 2022 Facebook. All rights reserved.
//

import WidgetKit
import SwiftUI
import Foundation


let DATA_KEY = "widgetTeleoptiKey";
let DATA_GROUP = "group.ru.nasvyazi";

struct WidgetInfo : Identifiable, Decodable, Encodable {
  let contractTime: String
  let description: String
  let alpha: Double
  let blue: Double
  let green: Double
  let red: Double
  var id : String {description}
}

struct WidgetTransferData: Decodable, Encodable {
    let data: [String]
    let updateDate: String
    let hasTeleopti: Bool
}

//                                            Example to create widget extension:

// struct Provider: TimelineProvider {
//     func placeholder(in context: Context) -> SimpleEntry {
//       let asd = SimpleEntry(date: Date(), text: "placeholder", widgetData:[])
//        return asd
//     }

//     func getSnapshot(in context: Context, completion: @escaping (SimpleEntry) -> ()) {
//         let entry = SimpleEntry(date: Date(), text: "getSnapshot", widgetData:[])
//         completion(entry)
//     }

//   //get
//     func getTimeline(in context: Context, completion: @escaping (Timeline<Entry>) -> ()) {
//       func getWidgetData() -> WidgetTransferData{
//          let transferData = (UserDefaults(suiteName: DATA_GROUP)?.string(forKey: DATA_KEY) ?? nil)
//          let jsonData = transferData!.data(using: .utf8)!
//          let data: WidgetTransferData = try! JSONDecoder().decode(WidgetTransferData.self, from: jsonData)
//         return data;
//       }
      
//       func getWidgetDataList(transferData: WidgetTransferData) -> [WidgetInfo]{
//         var dataList: [WidgetInfo] = [WidgetInfo]();
//         let receivedList = transferData.data;
        
//         for receivedItem in receivedList {
//             let jsonData = receivedItem.data(using: .utf8)!
//             let dataItem: WidgetInfo = try! JSONDecoder().decode(WidgetInfo.self, from: jsonData)
//           dataList.append(dataItem)
//         }
//         return dataList;
//       }
      
// // reload widget:
// //      WidgetCenter.shared.reloadTimelines(ofKind: "/*Your widget kind*/")
      
      
//       let data = getWidgetDataList(transferData: getWidgetData())
//         var entries: [SimpleEntry] = []

//         // Generate a timeline consisting of five entries an hour apart, starting from the current date.
//         let entryText = "HELLO WIDGET"
//         let currentDate = Date()
//         for hourOffset in 0 ..< 5 {
//           let entryDate = Calendar.current.date(byAdding: .hour, value: hourOffset, to: currentDate)!
//             let entry = SimpleEntry(date: entryDate, text: entryText, widgetData:data)
//             entries.append(entry)
//         }

//       let timeline = Timeline(entries: entries, policy: .atEnd)
//         completion(timeline)
      
//     }
// }

// struct SimpleEntry: TimelineEntry {
//     let date: Date
//     let text: String
//     let widgetData: Array<WidgetInfo>
// }

// struct WidgetTeleoptiEntryView : View {
// //  @frozen struct VStack<Content> where Content : View
//     var entry: Provider.Entry

//     var body: some View {
//       VStack(
//               alignment: .leading,
//               spacing: 10
//           ) {
//               ForEach(entry.widgetData) {item in
//                 let transparentTeal = Color(.sRGB, red: item.red/255, green: item.green/255, blue:item.blue/255, opacity: item.alpha/255)
//                 Text(item.contractTime)
//                 Text(item.description).background(transparentTeal)
        
//               }
//           }
//     }
// }

// @main
// struct WidgetTeleopti: Widget {
//     let kind: String = "WidgetTeleopti"
//     var body: some WidgetConfiguration {
//         StaticConfiguration(kind: kind, provider: Provider()) { entry in
//             WidgetTeleoptiEntryView(entry: entry)
//         }
//         .configurationDisplayName("My Widget")
//         .description("This is an example widget.")
//     }
// }

// struct WidgetTeleopti_Previews: PreviewProvider {
//     static var previews: some View {
//       WidgetTeleoptiEntryView(entry: SimpleEntry(date: Date(), text:"WidgetTeleopti_Previews", widgetData:[]))
//             .previewContext(WidgetPreviewContext(family: .systemSmall))
//     }
// }
