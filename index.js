"use strict";

import { NativeModules, Platform } from "react-native";

const { WidgetShareData: Widget } = NativeModules;

let params = null;

let WidgetData = {
  setDataList: async (dataList) => {},
  setParams: (params) => {},
};

const formatData = (dataList) => {
  try {
    let _dataList = [];
    if (Platform.OS == "android") {
      _dataList = dataList.map((item) => {
        let _item = { ...item };
        return _item;
      });
    } else {
      _dataList = dataList.map((item) => {
        let _item = JSON.stringify(item);
        return _item;
      });
    }
    if (Platform.OS == "android") {
      return JSON.stringify({ json: _dataList });
    } else {
      return _dataList;
    }
  } catch (err) {
    console.log(err);
  }
};

let isActive = false;
let storage = [];

WidgetData.setDataList = async (dataList) => {
  if (params == null) {
    throw "You should set params before use it via setParams(params)";
  }
  try {
    if (isActive) {
      return await new Promise((resolve) => {
        storage.push({
          resolve,
          list: dataList,
        });
      });
    }

    isActive = true;

    let _dataList = formatData(dataList);
    let result = null;

    if (Platform.OS == "android") {
      result = await Widget.setDataList(_dataList);
    } else {
      try {
        result = await Widget.setDataList(
          _dataList,
          params.ios.EXTENSION_ID,
          params.ios.DATA_GROUP,
          params.ios.DATA_KEY
        );
      } catch (error) {
        console.log(error);
      }
    }
    return result;
  } catch (error) {
    console.log(error);
    throw error;
  } finally {
    isActive = false;
    if (storage.length > 0) {
      let newList = [];
      let callbacks = [];
      storage.forEach((item) => {
        newList = newList.concat(item.list);
        callbacks.push(item.resolve);
      });
      storage = [];

      let result = await WidgetData.setDataList(newList);
      callbacks.forEach((resolve) => {
        resolve(result);
      });
    }
  }
};

//iOS only
WidgetData.setParams = async (_params) => {
  params = _params;
};

export default WidgetData;
