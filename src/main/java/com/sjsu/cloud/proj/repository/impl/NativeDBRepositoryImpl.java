package com.sjsu.cloud.proj.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.sjsu.cloud.proj.model.UpdateFile;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.repository.NativeDBRepository;


public class NativeDBRepositoryImpl implements NativeDBRepository  {
	
	@Autowired
	EntityManager entityManager;
	
	public User getUserInfo(String username, String password) {
		
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<User> cq = cb.createQuery(User.class);
	    Root<User> projeto = cq.from(User.class);
	    cq.select(projeto);
	    cq.where(cb.and(
	        cb.equal(projeto.get("email_id"), username),
	        cb.equal(projeto.get("password"), password)
	    ));
	    TypedQuery<User> query = entityManager.createQuery(cq);
	    try {
	    		return query.getSingleResult();	
	    } catch (NoResultException ex) {
	    		return null; 
	    }	    
	}
	
	public User getUserInfo(String email) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> projeto = cq.from(User.class);
		cq.where(cb.equal(projeto.get("email_id"), email));
		TypedQuery<User> query = entityManager.createQuery(cq);
	    try {
	    		return query.getSingleResult();	
	    } catch (NoResultException ex) {
	    		return null; 
	    }	    
	}
	
	public List<UpdateFile> getUserFiles(int userid){
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<UpdateFile> cq = cb.createQuery(UpdateFile.class);
		Root<UpdateFile> projeto = cq.from(UpdateFile.class);
		cq.where(cb.equal(projeto.get("userid"), userid));
		TypedQuery<UpdateFile> query = entityManager.createQuery(cq);		
		List<UpdateFile> list = query.getResultList();
		return list;
	}
	
	public List<UpdateFile> getAllUserFiles(){
		
		CriteriaQuery<UpdateFile> criteria = entityManager.getCriteriaBuilder().createQuery(UpdateFile.class);
	    criteria.select(criteria.from(UpdateFile.class));
	    List<UpdateFile> ListOfEmailDomains = entityManager.createQuery(criteria).getResultList();
	    return ListOfEmailDomains;
	}

	@Override
	@Transactional 
	public int deleteFile(String key) {
		
		CriteriaBuilder criteria  = entityManager.getCriteriaBuilder();
		CriteriaDelete<UpdateFile> query = criteria.createCriteriaDelete(UpdateFile.class);
		Root<UpdateFile> root = query.from(UpdateFile.class);
		query.where(root.get("path").in(key));
		return entityManager.createQuery(query).executeUpdate();
	}
}