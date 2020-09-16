package com.example.beerday;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "BeerDay.db";
    static final int DATABASE_VERSION = 1;

    // BEER TABLE
    static final String TABLE_BEERS = "Beers";
    static final String COL0 = "_id";
    static final String COL1 = "beer_name";
    static final String COL2 = "time";
    static final String COL3 = "counter";

    // RECIPE TABLE
    static final String TABLE_RECIPES = "Recipes";
    static final String COL0R = "_id";
    static final String COL1R = "id_beer";
    static final String COL2R = "ing_name";
    static final String COL3R = "quantity";
    static final String COL4R = "unit";

    // MYINGREDIENTS TABLE
    static final String TABLE_MYINGREDIENTS = "MyIngredients";
    static final String COL0M = "_id";
    static final String COL2M = "mying_name";
    static final String COL3M = "myquantity";
    static final String COL4M = "myunit";

    // NOTE TABLE
    static final String TABLE_NOTE = "Note";
    static final String COL0N = "_id";
    static final String COL1N = "id_beer";
    static final String COL2N = "note";

    SQLiteDatabase db;

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableBeers = "CREATE TABLE Beers (_id integer PRIMARY KEY NOT NULL,"+
                " beer_name text NOT NULL, time integer NOT NULL, counter int DEFAULT 0);";
        db.execSQL(createTableBeers);

        db.execSQL("INSERT INTO Beers VALUES(0, 'Beer1', '60', 0)");
        db.execSQL("INSERT INTO Beers VALUES(1, 'Beer2', '50', 0)");
        db.execSQL("INSERT INTO Beers VALUES(2, 'Beer3', '80', 0)");
        db.execSQL("INSERT INTO Beers VALUES(4, 'Beer4', '20', 0)");
        //db.execSQL("INSERT INTO Beers VALUES(5, 'Beer5', '35', 0)");

        String createTableRecipes = "CREATE TABLE Recipes (_id integer PRIMARY KEY NOT NULL," +
                "id_beer integer NOT NULL, ing_name text NOT NULL, quantity double NOT NULL, unit text NOT NULL);";
        db.execSQL(createTableRecipes);

        db.execSQL("INSERT INTO Recipes VALUES(0, 0, 'a', '10', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(1, 0, 'b', '20', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(2, 0, 'c', '40', 'grams')");

        db.execSQL("INSERT INTO Recipes VALUES(3, 1, 'a', '10', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(4, 1, 'b', '20', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(5, 1, 'c', '30', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(6, 1, 'd', '30', 'grams')");

        db.execSQL("INSERT INTO Recipes VALUES(7, 2, 'a', '100', 'grams')");
        db.execSQL("INSERT INTO Recipes VALUES(8, 2, 'd', '90', 'grams')");

        String createTableMyIngredients = "CREATE TABLE MyIngredients (_id integer PRIMARY KEY NOT NULL,"+
                " mying_name text NOT NULL, myquantity double NOT NULL, myunit text NOT NULL);";
        db.execSQL(createTableMyIngredients);

        db.execSQL("INSERT INTO MyIngredients VALUES(0, 'a', '100', 'grams')");
        db.execSQL("INSERT INTO MyIngredients VALUES(1, 'b', '100', 'grams')");
        db.execSQL("INSERT INTO MyIngredients VALUES(2, 'c', '100', 'grams')");
        db.execSQL("INSERT INTO MyIngredients VALUES(3, 'd', '100', 'grams')");

        String createTableNote = "CREATE TABLE Note (_id integer PRIMARY KEY NOT NULL," +
                " id_beer int NOT NULL, note text);";
        db.execSQL(createTableNote);

        db.execSQL("INSERT INTO Note VALUES(0, 0 , 'This beer is just an example. Also this note is not meant to give any information about the way to cook the beer. \n \n Ingredients needed: \n - Grains 300 grams; \n - Malt 50 grams; \n - Sugar 150 grams;')");
        db.execSQL("INSERT INTO Note VALUES(1, 1 , '')");
        db.execSQL("INSERT INTO Note VALUES(2, 2 , 'questo messaggio è una prova')");
        db.execSQL("INSERT INTO Note VALUES(3, 4 , '')");
        //db.execSQL("INSERT INTO Note VALUES(4, 5 , '')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYINGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
    }

    //---------------Insert a new beer method (only name and time for now)--------------------------------------------------------------------------------

    public void addBeer(Beer b){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from Beers";
        Cursor cursor = db.rawQuery(query, null);
        //int count = cursor.getCount(); THOSE TWO LINES ARE NOT NEEDED SINCE THE ID IS AUTOINCREMENT
        //values.put(COL0, count);
        values.put(COL1, b.getBeerName());
        values.put(COL2, b.getBeerTime());
        //values.put(COL3, 0); inserito di default ad ogni nuovo record

        Integer newID = null;
        ContentValues note = new ContentValues();
        Cursor max = db.rawQuery("SELECT MAX(id_beer) FROM Note", null);

        while (max.moveToNext()) {
            newID = max.getInt(0);
        }

        note.put(COL1N, newID + 1);
        note.put(COL2N, "");

        db.insert(TABLE_BEERS, null, values);
        db.insert(TABLE_NOTE, null, note);
        db.close();
    }


    //---------------Insert a new Ingredient into recipes method --------------------------------------------------------------------------------

    public void addIngredient(Recipe r){
        db = this.getWritableDatabase();

        String query = "select MAX(_id) from Beers";
        Cursor cursor = db.rawQuery(query, null);
        int maxID = (cursor.moveToFirst() ? cursor.getInt(0) : 0);

        ContentValues values = new ContentValues();

        //values2.put(COL0R, count2); //ID ingredient
        values.put(COL1R, maxID); // maxID is the max value found in the _id column
        values.put(COL2R, r.getIngName()); // NAME ingredient
        values.put(COL3R, r.getIngQuantity()); // QUANTITY
        values.put(COL4R, r.getIngUnit()); // UNIT

        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    //---------------Insert a new Ingredient into recipes that already exists --------------------------------------------------------------------------------

    public void addIngredientOld(Recipe r, Integer bID){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL1R, bID); // ID beer
        values.put(COL2R, r.getIngName()); // NAME ingredient
        values.put(COL3R, r.getIngQuantity()); // QUANTITY
        values.put(COL4R, r.getIngUnit()); // UNIT

        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    //---------------------------Insert a new Ingredient of User method---------------------------------------------------------------------------------

    public void addMyIngredient(Ingredient i){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL2M, i.getMyIngName()); // NAME ingredient
        values.put(COL3M, i.getMyIngQuantity()); // QUANTITY
        values.put(COL4M, i.getMyIngUnit()); // UNIT

        db.insert(TABLE_MYINGREDIENTS, null, values);
        db.close();
    }

    //-----------------Check if the Edit Text are filled------------------------------------------------------------------------------------------------

    public boolean checkBeerName(String beername) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT beer_name FROM Beers", null);

        Boolean error = true;
        String bName;
        if (c.moveToFirst())
        {
            do {
                bName = c.getString(0);

                if (bName.equals(beername)) {
                    error = false;
                    break;
                }
            }
            while(c.moveToNext());
        }
        return error;
    }

    //---------------CHECK INGREDIENT NAME----------------------------------------------------------------------------------------------

    public boolean checkMyIngredientName(String ingName) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT mying_name FROM MyIngredients", null);

        Boolean error = true;
        String iName;
        if (c.moveToFirst())
        {
            do {
                iName = c.getString(0);

                if (iName.equals(ingName)) {
                    error = false;
                    break;
                }
            }
            while(c.moveToNext());
        }
        return error;
    }

    //-------------CHECK INGREDIENT OF THE RECIPE NAME---------------------------------------------------------------------------------------

    public boolean checkIngredientName(String ingName, Integer beerID) {
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT ing_name FROM "+TABLE_RECIPES+" WHERE " +COL1R+ "=" +beerID, null);

        Boolean error = true;
        String iName;
        if (c.moveToFirst())
        {
            do {
                iName = c.getString(0);

                if (iName.equals(ingName)) {
                    error = false;
                    break;
                }
            }
            while(c.moveToNext());
        }
        return error;
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    public String getBeerName(Integer id){
        db = this.getWritableDatabase();
        String name = null;
        Cursor c = db.rawQuery("SELECT beer_name FROM "+TABLE_BEERS+" WHERE " +COL0+ "=" +id, null);
        if (c.moveToFirst()) {
            name = c.getString(c.getColumnIndex(COL1));
        }
        
        return name;
    }

    public Cursor getBeerList(){
        db = this.getWritableDatabase();
        Cursor beers = db.rawQuery("SELECT * FROM " + TABLE_BEERS, null);
        return beers;
    }

    public Cursor getRecipeIngredientsList(Integer beerID){
        db = this.getWritableDatabase();
        //Cursor RecipeIng = db.rawQuery("SELECT * FROM " + TABLE_RECIPES + "WHERE" + COL1R + "=" + beerID + "", null);
        //Cursor RecipeIng = db.rawQuery("SELECT * FROM TABLE_RECIPES WHERE id_beer =" + beerID, null);
        //Cursor RecipeIng = db.rawQuery("SELECT "+COL2R+", "+COL3R+", "+COL4R+ " FROM "+TABLE_RECIPES+" WHERE "+COL1R+" = "+ beerID , null);
        Cursor RecipeIng = db.rawQuery("SELECT * FROM "+TABLE_RECIPES+" WHERE "+COL1R+" = "+ beerID , null);

        return RecipeIng;
    }

    public Cursor getMyIngredientsList(){
        db = this.getWritableDatabase();
        Cursor myIngredeints = db.rawQuery("SELECT * FROM " + TABLE_MYINGREDIENTS, null);
        return myIngredeints;
    }

    public Cursor getAllIngredientsList(){
        db = this.getWritableDatabase();
        Cursor allIngredeints = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
        return allIngredeints;
    }

    public Integer getLastBeerID (){
        db = this.getWritableDatabase();

        String query = "select MAX(_id) from Beers";
        Cursor cursor = db.rawQuery(query, null);
        int maxID = (cursor.moveToFirst() ? cursor.getInt(0) : 0);
        int ID = maxID + 1; // because it does not count the last beer just added

        return ID;
    }

    public Cursor getOneIngredient(Integer id){
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_MYINGREDIENTS+" WHERE " +COL0M+ "=" +id, null);
        return c;
    }

    public Cursor getOneRecipeIngredient(Integer id){
        db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_RECIPES+" WHERE " +COL0R+ "=" +id, null);
        return c;
    }

    public Cursor getNote(Integer id) {
        db = this.getWritableDatabase();

        Cursor note = db.rawQuery("SELECT note FROM " + TABLE_NOTE + " WHERE " + COL1N + " = " + id, null);

        return note;
    }

    //---------------DELETE BEER METHOD-------------------------------------------------------------------------------------------------------------

    public void deleteBeer(Integer beerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BEERS, COL0 +"=" + beerID, null);
        db.delete(TABLE_RECIPES, COL1R +"=" + beerID, null);
        db.close();
    }

    //---------------DELETE SINGLE INGREDIENT METHOD-------------------------------------------------------------------------------------------------------------

    public void deleteIngredient(Integer ingredientID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MYINGREDIENTS, COL0M +"=" + ingredientID, null);
        db.close();
    }

    //---------------DELETE ALL INGREDIENTS METHOD-------------------------------------------------------------------------------------------------------------

    public void deleteAllIngredient() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MYINGREDIENTS, null, null);
        db.close();
    }

    //---------------DELETE SINGLE BEER INGREDIENT METHOD-------------------------------------------------------------------------------------------------------------

    public void deleteRecipeIngredient(Integer ingredientID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COL0R +"=" + ingredientID, null);
        db.close();
    }

//------------------------------------------------------------------------------------------------------------------------------------------------------

    public Integer chekAvailability(ArrayList NamesIB, ArrayList NamesMyI) {
        Integer count = 0;

        for (int i = 0; i < NamesIB.size(); i = i + 2) {   // the +2 is due to the fact that the arrayList saves all the data in column. It means that the names have even index and quantities have odd index
            for (int j = 0; j < NamesMyI.size(); j = j +2) {
                //Integer bQ = Integer.parseInt(NamesIB.get(i+1).toString());
                //Integer iQ = Integer.parseInt(NamesMyI.get(j+1).toString());

                Double bQ = Double.parseDouble(NamesIB.get(i+1).toString());
                Double iQ = Double.parseDouble(NamesMyI.get(j+1).toString());

                if (NamesIB.get(i).equals(NamesMyI.get(j)) && bQ <= iQ) {
                    count = count + 2;
                }
            }
        }
        return count;
    }


    public void subtractQuantities(ArrayList NamesIB, ArrayList NamesMyI) {
        db = this.getWritableDatabase();

        for (int i = 0; i < NamesIB.size(); i = i + 2) {
            for (int j = 0; j < NamesMyI.size(); j = j +2) {
                Double bQ = Double.parseDouble(NamesIB.get(i+1).toString());
                Double iQ = Double.parseDouble(NamesMyI.get(j+1).toString());
                String[] name = new String[] {String.valueOf(NamesIB.get(i))};//nome ingrediente in considerazione
                String where = "mying_name = ?";
                ContentValues iLeft = new ContentValues();
                Double sub = iQ - bQ;

                if (NamesIB.get(i).equals(NamesMyI.get(j))) {
                    iLeft.put(COL3M,iQ-bQ);
                    db.update(TABLE_MYINGREDIENTS, iLeft, where , name);
                }
                if (sub == 0){
                    db.delete(TABLE_MYINGREDIENTS, where, name);
                }
            }
        }
    }

    public void updateMyIngredient(Integer ID, Ingredient i){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2M, i.getMyIngName());
        values.put(COL3M, i.getMyIngQuantity());
        values.put(COL4M, i.getMyIngUnit());
        String where = "_id = ?";
        String[] id = new String[] {String.valueOf(ID)};

        db.update(TABLE_MYINGREDIENTS, values, where, id);

    }

    public void updateRecipeIngredient(Integer ID, Recipe r){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2R, r.getIngName());
        values.put(COL3R, r.getIngQuantity());
        values.put(COL4R, r.getIngUnit());
        String where = "_id = ?";
        String[] id = new String[] {String.valueOf(ID)};

        db.update(TABLE_RECIPES, values, where, id);

    }

    public void updateNote(Integer ID, String n) {
        db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(COL2N, n);
        String where = "id_beer = ?";
        String[] id = new String[]{String.valueOf(ID)};

        db.update(TABLE_NOTE, value, where, id);

    }

    //--------------------RECOMMENDATION METHODS-----------------------------------------------------------------------------------------------------

    public void updateCounter (Integer beerID, ArrayList beerData){
        db = this.getWritableDatabase();

        for (int i = 0; i < beerData.size(); i = i + 2){
            Integer beer_id = Integer.parseInt(beerData.get(i).toString());
            Integer beer_counter = Integer.parseInt(beerData.get(i+1).toString());
            ContentValues newCounter = new ContentValues();
            String where = "_id = ?";
            String[] ID = new  String[] {String.valueOf(beer_id)};

            if (beerID == beer_id){
                newCounter.put(COL3,5);
            }else switch (beer_counter){
                case 0:
                    newCounter.put(COL3, 0);
                    break;
                case 1:
                    newCounter.put(COL3, 0);
                    break;
                case 2:
                    newCounter.put(COL3, 1);
                    break;
                case 3:
                    newCounter.put(COL3, 2);
                    break;
                case 4:
                    newCounter.put(COL3, 3);
                    break;
                case 5:
                    newCounter.put(COL3, 4);
                    break;
            }
            db.update(TABLE_BEERS, newCounter, where, ID);
        }
    }


    public Double getBeta(Integer beer_counter){
        //ArrayList<Integer> beta = new ArrayList<>();
        Double beta = 0.0;

            switch (beer_counter){
                case 0:
                    beta = (10.00);
                    break;
                case 1:
                    beta = (8.00);
                    break;
                case 2:
                    beta = (6.00);
                    break;
                case 3:
                    beta = (4.00);
                    break;
                case 4:
                    beta = (2.00);
                    break;
                case 5:
                    beta = (1.00);
                    break;
            }
        return beta;
    }

    public Double getAlpha (ArrayList NamesIB, ArrayList NamesMyI){
        Double alpha = 10.00;
        ArrayList<Double> percentage = new ArrayList<>();

        for (int i = 0; i < NamesIB.size(); i = i + 2) {
            for (int j = 0; j < NamesMyI.size(); j = j +2) {
                Double bQ = Double.parseDouble(NamesIB.get(i+1).toString()); // quantity of the recipe ingredient
                Double iQ = Double.parseDouble(NamesMyI.get(j+1).toString()); // quantity of my ingredient

                if (NamesIB.get(i).equals(NamesMyI.get(j))) {
                    percentage.add((bQ/iQ));
                }
            }
        }

        for (int i = 0; i < percentage.size(); i++){
            alpha = alpha + (percentage.get(i));
        }
        return alpha;
    }
}
