import {StyleSheet, View, Text, Platform, FlatList, ScrollView} from 'react-native';
import React, {useEffect} from 'react';

interface Props {
  //是否锁定列
  lockColumn?: boolean;
  //是否锁定行
  lockRow?: boolean;
  //标题数据
  titleData: string[];
  //数据源
  tableData: object[];
  //单元格文字大小
  textSize?: number;
  //单元格文字颜色
  textColor?: string;
  //单元格内边距
  cellPadding?: number;
  //第一行背景色
  firstRowBackGroundColor?: string;
  //表头字体颜色
  tableHeadTextColor?: string;
}

/**
 * 注释: 双向锁定表格
 * 时间: 2020/7/21 0021 14:06
 * @author 郭翰林
 * @param props
 * @constructor
 */
export default function LockTableView(props: Props) {
  const border_width = Platform.OS === 'ios' ? StyleSheet.hairlineWidth : StyleSheet.hairlineWidth * 2;
  let columnMaxWidth: number[] = [];
  //计算每列最大宽度
  props.tableData.map((item, i) => {
    Object.values(item).map((value, j) => {
      if (i === 0) {
        columnMaxWidth[j] = value.length * 15;
      } else {
        if (columnMaxWidth[j] < value.length * 15) {
          columnMaxWidth[j] = value.length * 15;
        }
      }
    });
  });
  /**
   * 注释: 绘制每行数据
   * 时间: 2020/7/23 0023 9:14
   * @author 郭翰林
   */
  function renderRowCell(rowData: object, index: number) {
    let childrenViews = [];
    Object.values(rowData).map((item, i) => {
      childrenViews.push(
        <Text
          style={{
            fontSize: 14,
            color: '#333',
            width: columnMaxWidth[i],
            borderWidth: border_width,
            borderColor: '#e7e7e7',
          }}>
          {item}
        </Text>,
      );
    });
    return <View style={{flexDirection: 'row', alignItems: 'center'}}>{childrenViews}</View>;
  }

  return (
    <View style={{flex: 1}}>
      <ScrollView horizontal={true} removeClippedSubviews={true} keyboardShouldPersistTaps={'handled'}>
        <FlatList
          data={props.tableData}
          showsHorizontalScrollIndicator={false}
          showsVerticalScrollIndicator={false}
          renderItem={rowData => {
            return renderRowCell(rowData.item, rowData.index);
          }}
        />
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({});
