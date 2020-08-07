### 功能介绍

一个可以锁定双向的 React Native 表格控件<br>
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5f2db5e7687f40b19adef4caa302a305~tplv-k3u1fbpfcp-zoom-1.image)

### 使用方法

```js
npm install react-native-locktable
```

或

```js
yarn add react-native-locktable
```

```js
import LockTableView from 'react-native-locktable';
```

### Api 介绍

```js
interface Props {
  //是否开启锁定
  isLockTable?: boolean;
  //标题数据
  titleData: string[];
  //数据源
  tableData: object[];
  //单元格文字大小
  textSize?: number;
  //单元格文字颜色
  textColor?: string;
  //单元格最大宽度
  cellMaxWidth?: number;
  //单元格高度
  cellHeight?: number;
  //第一行背景色
  firstRowBackGroundColor?: string;
  //第一列背景色
  firstColumnBackGroundColor?: string;
  //表头字体颜色
  tableHeadTextColor?: string;
}
```

```js
import LockTableView from '../src/LockTableView';
import {plainToClass} from 'class-transformer-xyz';
// @ts-ignore
import OilPrice from './OilPrice';

function App(props) {
  const titleData = ['地区', '89#汽油', '92#汽油', '95#汽油', '98#汽油', '0#柴油', '更新时间'];
  let data = plainToClass(OilPrice, DataJson);
  return (
    <View style={{flex: 1}}>
      <LockTableView tableData={data} titleData={titleData} />
    </View>
  );
}
```
