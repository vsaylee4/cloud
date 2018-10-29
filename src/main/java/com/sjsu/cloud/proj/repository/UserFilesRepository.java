package com.sjsu.cloud.proj.repository;

import org.springframework.data.repository.CrudRepository;
import com.sjsu.cloud.proj.model.UpdateFile;

public interface UserFilesRepository extends CrudRepository<UpdateFile, String>{

}
