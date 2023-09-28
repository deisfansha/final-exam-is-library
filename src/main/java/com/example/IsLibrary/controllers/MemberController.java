package com.example.IsLibrary.controllers;

import com.example.IsLibrary.dto.request.DtoMemberRequest;
import com.example.IsLibrary.dto.response.DtoMemberResponse;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;
    private Response response = new Response();

    @PostMapping("")
    public ResponseEntity createdMember(@RequestBody DtoMemberRequest member){
        Boolean added = memberService.addMember(member, response);
        if (added){
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/page")
    public ResponseEntity pageViewAll(@RequestParam int page, @RequestParam int limit){
        Page<DtoMemberResponse> memberList = memberService.pageView(page, limit);
        response.setMessage("Success");
        response.setData(memberList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity updatedMember(@PathVariable String code, @RequestBody DtoMemberRequest member){
        Boolean updated = memberService.updateMember(code, member, response);
        if (!updated){
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity softDeleteMember(@PathVariable String code){
        Boolean deleted = memberService.softDelete(code, response);
        if (!deleted){
            response.setData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }else {
            response.setMessage("Success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

}
