package kr.pah.pcs.pcscoin.domain.user.repository;

import kr.pah.pcs.pcscoin.domain.user.domain.StudentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentIdRepository extends JpaRepository<StudentId, Long> {
}
