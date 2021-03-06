package com.degla.db.models;


import com.degla.utils.AnnotatingModel;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.io.Serializable;


@Table(name="TBL_ENTITY")
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TYPE",columnDefinition="nvarchar(300)",discriminatorType=DiscriminatorType.STRING)
public class EntityEO implements Serializable , AnnotatingModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    protected int id = -1;



    @Override
    public boolean equals(Object obj) {

        if(obj instanceof EntityEO)
        {
            if(((EntityEO)obj).getId() == this.getId())
                return true;
            else return false;
        }
        return false;

    }





    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public Object getRowKey() {
        return this.getId();
    }
}
