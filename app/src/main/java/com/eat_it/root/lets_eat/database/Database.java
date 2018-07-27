package com.eat_it.root.lets_eat.database;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.eat_it.root.lets_eat.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteAssetHelper {
    private static final String db_name = "letseatorder.db";
    private static final int db_version = 1;


    public Database(Context context) {
        super(context, db_name, null, db_version);
    }


    public List<Order> getcart() {

        SQLiteDatabase db= getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();




        String[] sqlselect = {"ProductId","ProductName","Quantity","Discount","Price"};
        String table="orderdetail";

        qb.setTables(table);


        Cursor cursor = qb.query(db,sqlselect,null,null,null,null,null);
        final List<Order> result = new ArrayList<>();


        if(cursor.moveToFirst()){
            do {
              result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductId")),
                      cursor.getString(cursor.getColumnIndex("ProductName")),
                      cursor.getString(cursor.getColumnIndex("Quantity")),
                      cursor.getString(cursor.getColumnIndex("Discount")),
                      cursor.getString(cursor.getColumnIndex("Price"))
              ));
            }while (cursor.moveToNext());

        }
        return result;
    }

    public void addtocart(Order order){

        SQLiteDatabase db=getWritableDatabase();
        String query=String.format("INSERT INTO orderdetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",

                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
      db.execSQL(query);


    }

    public void clearcart(){

        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM orderdetail");
        db.execSQL(query);


    }
}


    /*private static final String db_name="letseatorder";
    private static final int db_version=1;

   *//* String sqltable="CREATE TABLE if not exists `orderdetail` (\n" +
            "\t`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
            "\t`ProductId`\tTEXT,\n" +
            "\t`ProductName`\tTEXT,\n" +
            "\t`Quantity`\tTEXT,\n" +
            "\t`Price`\tTEXT,\n" +
            "\t`Discount`\tTEXT\n" +
            ")";*//*


    public Database(Context context) {
        super(context, db_name, null, db_version);
        getWritableDatabase().execSQL(sqltable);

    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List <Order> getcart(){


        String sqlselect="Select * from orderdetail";
        Cursor cursor=getReadableDatabase().rawQuery(sqlselect,null);
        final List<Order> result=new ArrayList<>();


            while (cursor.moveToNext()) {
                Order order = new Order();
                order.setProductId(cursor.getString(cursor.getColumnIndex("ProductId")));
                order.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                order.setDiscount(cursor.getString(cursor.getColumnIndex("Discount")));
                order.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
                order.setQuantity(cursor.getString(cursor.getColumnIndex("Quantity")));
                result.add(order);

            }



        return result;
    }


    public void addtocart(Order order){

        String query=String.format("INSERT INTO orderdetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
       getWritableDatabase().execSQL(query);


    }

    public void cleancart(){

        String query=String.format("DELETE FROM orderdetail");
        getWritableDatabase().execSQL(query);


    }*/


