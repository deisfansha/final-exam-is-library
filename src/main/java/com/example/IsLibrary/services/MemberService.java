package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoMemberRequest;
import com.example.IsLibrary.dto.response.DtoMemberResponse;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.repositories.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepo memberRepo;

    public Boolean addMember(DtoMemberRequest memberRequest, Response response){
        Optional<Member> existingNumber = memberRepo.findByPhoneNumberAndIsDeletedIsFalse(memberRequest.getPhoneNumber());
        List<Member> getLastId = memberRepo.findLast();
        Member lastMember = getLastId.get(0);
        if ((memberRequest.getName().isEmpty() || memberRequest.getGender().isEmpty()) || memberRequest.getPhoneNumber().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (!isPhoneNumberValid(memberRequest.getPhoneNumber())){
            response.setMessage("Format Number appropriate");
            return false;
        } else if (existingNumber.isPresent()) {
            response.setMessage("Phone Number is already exists");
            return false;
        }

        int code;
        if (getLastId.isEmpty()){
            code = 5;
        }else {
            code = Math.toIntExact(lastMember.getId() + 5);
        }

        String codeNumber = "00"+code;

        Member member = new Member();
        member.setGender(memberRequest.getGender());
        member.setName(memberRequest.getName());
        member.setPhoneNumber(memberRequest.getPhoneNumber());
        member.setCodeMember(codeNumber);
        memberRepo.save(member);
        response.setData(new DtoMemberResponse(member.getCodeMember(), member.getName(),  member.getGender(), member.getPhoneNumber()));
        return true;
    }

    public Page<DtoMemberResponse> pageView(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Member> result =  memberRepo.findAllByIsDeletedIsFalseOrderByIdAsc(pageable);
        return new PageImpl(result.getContent(), PageRequest.of(page, limit), result.getTotalPages());
    }

    public Boolean updateMember(String code, DtoMemberRequest memberRequest, Response response){
        Optional<Member> existingMember = memberRepo.findByCodeMemberAndIsDeletedIsFalse(code);
        Optional<Member> existingNumber = memberRepo.findByPhoneNumberAndIsDeletedIsFalse(memberRequest.getPhoneNumber());

        if (!existingMember.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        } else if (!isPhoneNumberValid(memberRequest.getPhoneNumber())){
            response.setMessage("Format Number appropriate");
            return false;
        } else if (existingNumber.isPresent()) {
            response.setMessage("Phone Number is already exists");
            return false;
        }

        if (existingMember.get().getName().equalsIgnoreCase(memberRequest.getName()) &&
                existingMember.get().getPhoneNumber().equalsIgnoreCase(memberRequest.getPhoneNumber())){
            response.setMessage("Data No Changes");
        } else {
            existingMember.get().setName(memberRequest.getName());
            existingMember.get().setPhoneNumber(memberRequest.getPhoneNumber());
            memberRepo.save(existingMember.get());
            response.setMessage("Success");
        }

        response.setData(new DtoMemberResponse(existingMember.get().getCodeMember(),
                existingMember.get().getName(),
                existingMember.get().getGender(),
                existingMember.get().getPhoneNumber()));
        return true;
    }

    public Boolean softDelete(String code, Response response){
        Optional<Member> existingMember = memberRepo.findByCodeMemberAndIsDeletedIsFalse(code);

        if (!existingMember.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        }

        existingMember.get().setDeleted(true);
        memberRepo.save(existingMember.get());
        response.setData(new DtoMemberResponse(existingMember.get().getCodeMember(),
                existingMember.get().getName(),
                existingMember.get().getGender(),
                existingMember.get().getPhoneNumber()));

        return true;
    }

    private Boolean isPhoneNumberValid(String phoneNumber){
        return phoneNumber.matches("^[0-9]{8,13}$");
    }
}
