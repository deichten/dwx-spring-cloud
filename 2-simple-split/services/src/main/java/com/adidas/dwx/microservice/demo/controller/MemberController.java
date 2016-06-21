package com.adidas.dwx.microservice.demo.controller;

import com.adidas.dwx.microservice.demo.model.Location;
import com.adidas.dwx.microservice.demo.model.Member;
import com.adidas.dwx.microservice.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


@Controller
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Member>> getAllMembersHavingCities() {
        return new ResponseEntity<List<Member>>(memberService.getAllMembersWithEntries(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{memberName}", method = RequestMethod.GET)
    public ResponseEntity<Member> getMember(@PathVariable String memberName) {
        Optional<Member> memberOptional = memberService.findOne(memberName);

        if (memberOptional.isPresent()) {
            return new ResponseEntity<Member>(memberOptional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<Member>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createMember(@RequestBody Member member) {

        // check for duplicate
        Optional<Member> memberOpt = memberService.findOne(member.getName());
        if (memberOpt.isPresent()) {
            return ResponseEntity.status(409).location(
                UriComponentsBuilder
                    .fromPath("/members/{name}")
                    .build()
                    .expand(memberOpt.get().getName())
                    .toUri()
            ).build();
        }

        return ResponseEntity.created(
            UriComponentsBuilder
                .fromPath("/members/{name}")
                .build()
                .expand(new Object[]{memberService.upsert(member).getName()})
                .toUri()
            ).build();
    }

    @RequestMapping(value = "/{memberName}", method = RequestMethod.POST)
    public ResponseEntity<Void> createMember(@PathVariable String memberName, @RequestBody Member member) {
        if (member.getName().equals(memberName)) {
            return createMember(member);
        }

        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/{memberName}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateMember(@PathVariable String memberName, @RequestBody Member member) {
        if (!member.getName().equals(memberName)) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Member> memberOpt = memberService.findOne(memberName);
        if (memberOpt.isPresent()) {
            memberService.upsert(member);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/{memberName}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeMember(@PathVariable String memberName) {
        if (memberService.removeById(memberName)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{memberName}/locations", method = RequestMethod.POST)
    public ResponseEntity<Void> addLocation(@PathVariable String memberName, @RequestBody Location location) {
        Optional<Member> memberOpt = memberService.findOne(memberName);
        if (!memberOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Member member = memberOpt.get();
        member.addLocation(location);

        Member updatedMember = memberService.upsert(member);
        if (member.getLocations().size() != updatedMember.getLocations().size()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }
}
