package com.adidas.dwx.microservice.demo.service;

import com.adidas.dwx.microservice.demo.model.Member;
import com.adidas.dwx.microservice.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SimpleMemberService implements MemberService {

    private MemberRepository repository;

    @Autowired
    public SimpleMemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<Member> getAllMembersWithEntries() {
        return repository.findAll();
    }

    public Optional<Member> findOne(String member) {
        return Optional.ofNullable(repository.findOne(member));
    }

    @Override
    public Member upsert(Member member) {
        if (member != null) {
            return repository.saveAndFlush(member);
        }
        return null;
    }

    public boolean removeById(String memberName) {
        if (repository.exists(memberName)) {
            repository.delete(memberName);
            if (!repository.exists(memberName)) {
                return true;
            }
        }

        return false;
    }
}
