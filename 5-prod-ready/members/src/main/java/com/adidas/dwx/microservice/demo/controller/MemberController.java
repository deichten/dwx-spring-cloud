package com.adidas.dwx.microservice.demo.controller;

import com.adidas.dwx.microservice.demo.model.Location;
import com.adidas.dwx.microservice.demo.model.Member;
import com.adidas.dwx.microservice.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private MemberRepository memberRepository;

    @Autowired
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @RequestMapping(value = "/{memberName}/locations", method = RequestMethod.POST)
    public ResponseEntity<Void> addLocation(@PathVariable String memberName, @RequestBody Location location) {
        Member memberOpt = memberRepository.findOne(memberName);
        if (memberOpt == null) {
            return ResponseEntity.notFound().build();
        }

        Member member = memberOpt;
        member.addLocation(location);

        Member updatedMember = memberRepository.saveAndFlush(member);
        if (member.getLocations().size() != updatedMember.getLocations().size()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

}
