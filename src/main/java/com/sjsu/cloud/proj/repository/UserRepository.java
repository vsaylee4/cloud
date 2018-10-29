package com.sjsu.cloud.proj.repository;

import org.springframework.data.repository.CrudRepository;
import com.sjsu.cloud.proj.model.UpdateFile;
import com.sjsu.cloud.proj.model.User;


public interface UserRepository extends CrudRepository<User, String>{

	void save(UpdateFile fileInfo);
}
