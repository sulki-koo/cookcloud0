package cookcloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;

public interface CodeRepository extends JpaRepository<Code, CodeId>{

}
