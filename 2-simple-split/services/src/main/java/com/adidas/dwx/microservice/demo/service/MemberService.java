package com.adidas.dwx.microservice.demo.service;

import com.adidas.dwx.microservice.demo.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    List<Member> getAllMembersWithEntries();

    Optional<Member> findOne(String member);

    Member upsert(Member member);

    boolean removeById(String memberName);
}
