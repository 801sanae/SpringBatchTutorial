package com.example.springbatchtutorial.repository;

import com.example.springbatchtutorial.domain.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.example.springbatchtutorial.repository
 * fileName       : AccountsRepository
 * author         : kmy
 * date           : 2023/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/29        kmy       최초 생성
 */
public interface AccountsRepository extends JpaRepository<Accounts, Integer> {
}
