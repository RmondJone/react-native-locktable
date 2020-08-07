/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {StyleSheet, View} from 'react-native';
// @ts-ignore
import DataJson from '../app.json';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import LockTableView from '../src/LockTableView';
import {plainToClass} from 'class-transformer-xyz';
// @ts-ignore
import OilPrice from './OilPrice';

function App(props) {
  const titleData = ['地区', '89#汽油', '92#汽油', '95#汽油', '98#汽油', '0#柴油', '更新时间'];
  let data = plainToClass(OilPrice, DataJson);
  return (
    <View style={{flex: 1}}>
      <LockTableView tableData={data} titleData={titleData} isLockTable={true} />
    </View>
  );
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
