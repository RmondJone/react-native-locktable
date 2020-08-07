/// <reference types="react" />
interface Props {
  isLockTable?: boolean;
  titleData: string[];
  tableData: object[];
  textSize?: number;
  textColor?: string;
  cellMaxWidth?: number;
  cellHeight?: number;
  firstRowBackGroundColor?: string;
  firstColumnBackGroundColor?: string;
  tableHeadTextColor?: string;
}
/**
 * 注释: 双向锁定表格
 * 时间: 2020/7/21 0021 14:06
 * @author 郭翰林
 * @param props
 * @constructor
 */
declare function LockTableView(props: Props): JSX.Element;
declare namespace LockTableView {
  var defaultProps: {
    isLockTable: boolean;
    textSize: number;
    textColor: string;
    tableHeadTextColor: string;
    cellMaxWidth: number;
    cellHeight: number;
    firstRowBackGroundColor: string;
    firstColumnBackGroundColor: string;
  };
}
export default LockTableView;
