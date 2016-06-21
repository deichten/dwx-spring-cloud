package com.adidas.dwx.microservice.demo.repository;

import com.adidas.dwx.microservice.demo.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
