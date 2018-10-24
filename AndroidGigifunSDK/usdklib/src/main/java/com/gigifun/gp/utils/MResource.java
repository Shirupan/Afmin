package com.gigifun.gp.utils;

import android.content.Context;

public class MResource {
	@SuppressWarnings("rawtypes")






	 public static int getIdByName(Context context, String className, String name) {   
		//return context.getResources().getIdentifier(name, className,context.getPackageName());
		
        String packageName = context.getPackageName();
        Class r = null;    
        int id = 0;    
        try{    
            r = Class.forName(packageName + ".R");    
            Class[] classes = r.getClasses();    
            Class desireClass = null;    
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {    
                    desireClass = classes[i];    
                    break;    
                }    
            }    
         if (desireClass != null)    
         id = desireClass.getField(name).getInt(desireClass);    
        } catch (ClassNotFoundException e) {    
            e.printStackTrace();    
        } catch (IllegalArgumentException e) {    
            e.printStackTrace();    
        } catch (SecurityException e) {    
            e.printStackTrace();    
        } catch (IllegalAccessException e) {    
            e.printStackTrace();    
        } catch (NoSuchFieldException e) {    
            e.printStackTrace();    
        }    
        return id;    
    }

    public static int[] getIdsByName(Context context, String className, String name) {


        //return context.getResources().getIntArray( context.getResources().getIdentifier(name, className, context.getPackageName()));
        String packageName = context.getPackageName();
        Class r = null;
        int[] ids = null;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null) && (desireClass.getField(name).get(desireClass).getClass().isArray()))
                ids = (int[])desireClass.getField(name).get(desireClass);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ids;
    }
}