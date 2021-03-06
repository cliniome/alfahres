package com.degla.utils;

/**
 * Created by snouto on 08/05/2015.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.degla.dao.AbstractDAO;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

@SuppressWarnings("serial")
public class GenericLazyDataModel<T extends AnnotatingModel> extends LazyDataModel<T> {


    private List<T> dataModels = new ArrayList<T>();
    private AbstractDAO<T> paginator;


   /* public GenericLazyDataModel(List<T> models)
    {
        this.dataModels = models;
    }*/

    public GenericLazyDataModel(AbstractDAO<T> paginatorModel)
    {
        this.paginator = paginatorModel;


    }

   /* private void loadSomeData()
    {
        this.dataModels = paginator.getPaginatedResults(0,getPageSize());
        this.setWrappedData(this.dataModels);
        this.setRowCount((int)paginator.getMaxResults());

    }*/

    //This is the default constructor
    /*public GenericLazyDataModel(){}*/

    public boolean addModel(T model)
    {
        if(dataModels == null) dataModels = new ArrayList<T>();

        return dataModels.add(model);
    }




    @Override
    public T getRowData(String rowKey) {
        for(T model : dataModels) {
            if(model.getRowKey().equals(rowKey))
                return model;
        }

        return null;
    }

    @Override
    public Object getRowKey(T model) {
        return model.getRowKey();
    }

    @Override
    public void setRowIndex(int rowIndex) {

        if(rowIndex == -1 || getPageSize() == 0)
        {

        }else  super.setRowIndex(rowIndex % getPageSize());

    }



    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try
        {

            List<T> data = new ArrayList<T>();
            //select the data
            this.dataModels = paginator.getPaginatedResults(first, pageSize);
            this.setRowCount((int) paginator.getMaxResults());

            //filter
            for(T model : dataModels) {
                boolean match = true;

                for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        String filterValue = filters.get(filterProperty).toString();
                        String methodName = "get"+Capitalize(filterProperty);
                        String fieldValue = String.valueOf(model.getClass().getMethod(methodName).invoke(model));

                        if(filterValue == null || fieldValue.startsWith(filterValue)) {
                            match = true;
                        }
                        else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }

                if(match) {
                    data.add(model);
                }
            }

            //sort
            if(sortField != null) {
                Collections.sort(data, new LazySorter(sortField, sortOrder));
            }

            //rowCount
            int dataSize = data.size();
            //this.setRowCount(dataSize);



            //paginate
            if(dataSize > pageSize) {
                try {
                    return data.subList(first, first + pageSize);
                }
                catch(IndexOutOfBoundsException e) {
                    return data.subList(first, first + (dataSize % pageSize));
                }
            }
            else {
                return data;
            }




        }catch(Exception s)
        {


            return null;
        }
    }

    public static String Capitalize(String line)
    {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
