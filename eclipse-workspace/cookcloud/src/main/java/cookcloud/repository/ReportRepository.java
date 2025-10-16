package cookcloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cookcloud.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{

}
