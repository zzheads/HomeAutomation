package com.zzheads.HomeAutomation.dao;//

import com.zzheads.HomeAutomation.exceptions.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

// HomeAutomation
// com.zzheads.HomeAutomation.dao created by zzheads on 20.08.2016.
//
@Repository
class CrudDaoImpl implements CrudDao {

    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired SessionFactory mSessionFactory;

    @Override
    public Object findById(Class mClass, Long id) throws DaoException {
        try {
            Session session = mSessionFactory.openSession();
            session.beginTransaction();
            Object o = session.get(mClass, id);
            session.close();
            return o;
        } catch (Exception ex) {
            throw new DaoException(ex, String.format("Problem finding %s class, %d id (findById method).", mClass,id));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public List findAll(Class mClass) throws DaoException {
        try {
            Session session = mSessionFactory.openSession();
            session.beginTransaction();
            List o = session.createCriteria(mClass).list();
            session.close();
            return o;
        } catch (Exception ex) {
            throw new DaoException(ex, String.format("Problem finding %s class (findAll method).", mClass));
        }
    }

    @Override
    public void save(Object o) throws DaoException {
        try {
            Session session = mSessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(o);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            throw new DaoException(ex, String.format("Problem saving %s object (save method).", o));
        }
    }

    @Override
    public void delete(Object o) throws DaoException {
        try {
            Session session = mSessionFactory.openSession();
            session.beginTransaction();
            session.delete(o);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            throw new DaoException(ex, String.format("Problem deleting %s object (delete method).", o));
        }
    }
}
