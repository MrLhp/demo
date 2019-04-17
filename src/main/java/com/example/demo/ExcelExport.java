package com.example.demo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExport {

    public static void exportExcel(List<ExcelData> list, int index) throws FileNotFoundException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("数据信息");
        HSSFRow hssfRowTitle = sheet.createRow(0);
        setExcelTitle(hssfRowTitle);
        for(int t = 0;t<list.size();t++){
            HSSFRow xssfR = sheet.createRow(t+1);
            for(int x=0;x<10;x++){
                HSSFCell hssfCell =  xssfR.createCell(x);
                switch (x){
                    case 0 :
                        hssfCell.setCellValue(list.get(t).getCinemaCode());
                        break;
                    case 1 :
                        hssfCell.setCellValue(list.get(t).getCinemaName());
                        break;
                    case 2:
                        hssfCell.setCellValue(list.get(t).getCinemaChainName());
                        break;
                    case 3:
                        hssfCell.setCellValue(list.get(t).getProvinceName());
                        break;
                    case 4 :
                        hssfCell.setCellValue(list.get(t).getOperation());
                        break;
                    case 5 :
                        hssfCell.setCellValue(list.get(t).getSessionCode());
                        break;
                    case 6:
                        hssfCell.setCellValue(list.get(t).getBusinessDate());
                        break;
                    case 7:
                        hssfCell.setCellValue(list.get(t).getSessionTime());
                        break;
                    case 8 :
                        hssfCell.setCellValue(list.get(t).getPrice());
                        break;
                    case 9 :
                        hssfCell.setCellValue(list.get(t).getService());
                        break;
                    default:
                        //...;
                        break;
                }

            }
        }

        FileOutputStream os = new FileOutputStream("/home/pc-yx/影院影票数据/影院影票数据" + index + ".xls");
        try {
            wb.write(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setExcelTitle(HSSFRow xssfTitle){
        HSSFCell hssfCell0 = xssfTitle.createCell(0);
        hssfCell0.setCellValue("影院编码");
        HSSFCell hssfCell1 = xssfTitle.createCell(1);
        hssfCell1.setCellValue("影院名称");
        HSSFCell hssfCell2 = xssfTitle.createCell(2);
        hssfCell2.setCellValue("院线名称");
        HSSFCell hssfCell3 = xssfTitle.createCell(3);
        hssfCell3.setCellValue("省份");
        HSSFCell hssfCell4 = xssfTitle.createCell(4);
        hssfCell4.setCellValue("操作");
        HSSFCell hssfCell5 = xssfTitle.createCell(5);
        hssfCell5.setCellValue("场次编码");
        HSSFCell hssfCell6 = xssfTitle.createCell(6);
        hssfCell6.setCellValue("营业日期");
        HSSFCell hssfCell7 = xssfTitle.createCell(7);
        hssfCell7.setCellValue("放映时间");
        HSSFCell hssfCell8 = xssfTitle.createCell(8);
        hssfCell8.setCellValue("价钱");
        HSSFCell hssfCell9 = xssfTitle.createCell(9);
        hssfCell9.setCellValue("手续费");
    }
}
