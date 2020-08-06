import { FlatList, Platform, ScrollView, StyleSheet, Text, View } from 'react-native';
import React, { useRef } from 'react';
/**
 * 注释: 双向锁定表格
 * 时间: 2020/7/21 0021 14:06
 * @author 郭翰林
 * @param props
 * @constructor
 */
export default function LockTableView(props) {
    const border_width = Platform.OS === 'ios' ? StyleSheet.hairlineWidth : StyleSheet.hairlineWidth * 2;
    let columnMaxWidth = [];
    let firstColumnData = [];
    //计算每列最大宽度、分割数据
    let scale = props.textSize;
    props.titleData.map((value, i) => {
        if (value.length * scale < props.cellMaxWidth) {
            columnMaxWidth[i] = value.length * scale;
        }
        else {
            columnMaxWidth[i] = props.cellMaxWidth;
        }
    });
    props.tableData.map((item, i) => {
        Object.values(item).map((value, j) => {
            if (j == 0) {
                firstColumnData.push(value);
            }
            if (columnMaxWidth[j] < value.length * scale) {
                if (value.length * scale < props.cellMaxWidth) {
                    columnMaxWidth[j] = value.length * scale;
                }
                else {
                    columnMaxWidth[j] = props.cellMaxWidth;
                }
            }
        });
        //删除对象第一个属性数据
        delete item[Object.keys(item)[0]];
    });
    /**
     * 注释: 绘制每行数据
     * 时间: 2020/7/23 0023 9:14
     * @author 郭翰林
     */
    function renderRowCell(rowData, index) {
        let childrenViews = [];
        Object.values(rowData).map((item, i) => {
            childrenViews.push(<Text style={{
                fontSize: props.textSize,
                color: props.textColor,
                width: columnMaxWidth[i + 1],
                height: props.cellHeight,
                textAlign: 'center',
                textAlignVertical: 'center',
                borderWidth: border_width,
                borderColor: '#e7e7e7',
            }}>
          {item}
        </Text>);
        });
        return <View style={{ flexDirection: 'row', alignItems: 'center' }}>{childrenViews}</View>;
    }
    /**
     * 注释: 绘制第一行行数据
     * 时间: 2020/8/5 0005 17:33
     * @author 郭翰林
     * @param rowData
     * @returns {any}
     */
    function renderFirstRowCell(rowData) {
        let childrenViews = [];
        rowData.map((item, i) => {
            childrenViews.push(<Text style={{
                fontSize: props.textSize,
                color: props.textColor,
                width: columnMaxWidth[i + 1],
                height: props.cellHeight,
                textAlign: 'center',
                textAlignVertical: 'center',
                borderWidth: border_width,
                borderColor: '#e7e7e7',
            }}>
          {item}
        </Text>);
        });
        return <View style={{ flexDirection: 'row', alignItems: 'center' }}>{childrenViews}</View>;
    }
    /**
     * 注释: 绘制表格头
     * 时间: 2020/8/5 0005 17:36
     * @author 郭翰林
     * @returns {any}
     */
    function renderHeaderView() {
        let first = props.titleData.shift();
        return (<View style={{ flexDirection: 'row', backgroundColor: props.firstRowBackGroundColor }}>
        <View style={{
            width: columnMaxWidth[0] + props.textSize,
            height: props.cellHeight,
            borderWidth: border_width,
            borderColor: '#e7e7e7',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
          <Text style={{
            fontSize: props.textSize,
            color: props.textColor,
            textAlign: 'center',
            textAlignVertical: 'center',
        }}>
            {first}
          </Text>
        </View>
        <ScrollView ref={headScrollView} style={{ borderRightWidth: border_width, borderColor: '#e7e7e7' }} horizontal={true} removeClippedSubviews={true} showsHorizontalScrollIndicator={false} showsVerticalScrollIndicator={false} bounces={false} scrollEnabled={false} keyboardShouldPersistTaps={'handled'}>
          {renderFirstRowCell(props.titleData)}
        </ScrollView>
      </View>);
    }
    /**
     * 注释: 绘制第一列锁定数据
     * 时间: 2020/8/5 0005 15:21
     * @author 郭翰林
     * @param rowData
     * @param index
     * @returns {any}
     */
    function renderFirstCell(rowData, index) {
        return (<View style={{
            alignItems: 'center',
            justifyContent: 'center',
            borderColor: '#e7e7e7',
            height: props.cellHeight,
            borderWidth: border_width,
        }}>
        <Text style={{
            fontSize: props.textSize,
            color: props.textColor,
            textAlign: 'center',
            textAlignVertical: 'center',
        }}>
          {rowData}
        </Text>
      </View>);
    }
    let lockList = useRef(null);
    let headScrollView = useRef(null);
    return (<View style={{ flex: 1 }}>
      {renderHeaderView()}
      <View style={{ flex: 1, flexDirection: 'row' }}>
        
        <View style={{ width: columnMaxWidth[0] + props.textSize }}>
          <FlatList ref={lockList} contentContainerStyle={{
        backgroundColor: props.firstColumnBackGroundColor,
    }} scrollEnabled={false} data={firstColumnData} showsHorizontalScrollIndicator={false} showsVerticalScrollIndicator={false} renderItem={rowData => {
        return renderFirstCell(rowData.item, rowData.index);
    }}/>
        </View>
        
        <ScrollView style={{ borderRightWidth: border_width, borderColor: '#e7e7e7' }} horizontal={true} bounces={false} onScroll={event => {
        headScrollView.current.scrollTo({ x: event.nativeEvent.contentOffset.x });
    }} keyboardShouldPersistTaps={'handled'}>
          <FlatList data={props.tableData} showsHorizontalScrollIndicator={false} showsVerticalScrollIndicator={false} onScroll={event => {
        lockList.current.scrollToOffset({
            animated: false,
            offset: event.nativeEvent.contentOffset.y,
        });
    }} renderItem={rowData => {
        return renderRowCell(rowData.item, rowData.index);
    }}/>
        </ScrollView>
      </View>
    </View>);
}
LockTableView.defaultProps = {
    textSize: 15,
    textColor: '#666',
    tableHeadTextColor: '#666',
    cellMaxWidth: 150,
    cellHeight: 35,
    firstRowBackGroundColor: '#F0F9FF',
    firstColumnBackGroundColor: '#FFF9F7',
};
const styles = StyleSheet.create({});
