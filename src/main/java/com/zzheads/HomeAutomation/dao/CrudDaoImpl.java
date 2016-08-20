package com.zzheads.HomeAutomation.dao;//

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
public class CrudDaoImpl implements CrudDao {

    @Autowired SessionFactory mSessionFactory;

    @Override
    public Object findById(Class mClass, Long id) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        Object o = session.get(mClass, id);
        session.close();
        return o;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object findByName(Class mClass, String name) {
        return findAll(mClass).stream().filter(r->r.toString().equals(name)).findFirst().get();
    }

    @SuppressWarnings("deprecation")
    @Override
    public List findAll(Class mClass) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        List o = session.createCriteria(mClass).list();
        session.close();
        return o;
    }

    @Override
    public void save(Object o) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(o);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Object o) {
        Session session = mSessionFactory.openSession();
        session.beginTransaction();
        session.delete(o);
        session.getTransaction().commit();
        session.close();
    }
}
