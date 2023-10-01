package com.example.IsLibrary.services;

import com.example.IsLibrary.dto.request.DtoMemberRequest;
import com.example.IsLibrary.dto.response.DtoMemberResponse;
import com.example.IsLibrary.models.Member;
import com.example.IsLibrary.models.Response;
import com.example.IsLibrary.models.Transaction;
import com.example.IsLibrary.repositories.MemberRepo;
import com.example.IsLibrary.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private TransactionRepo transactionRepo;

    public Boolean addMember(DtoMemberRequest memberRequest, Response response){
        Optional<Member> existingNumber = memberRepo.findByPhoneNumberAndIsDeletedIsFalse(memberRequest.getPhoneNumber());

        if ((memberRequest.getName().isEmpty() || memberRequest.getGender().isEmpty()) || memberRequest.getPhoneNumber().isEmpty()){
            response.setMessage("Data must be filled in");
            return false;
        } else if (!isPhoneNumberValid(memberRequest.getPhoneNumber())){
            response.setMessage("Format Number appropriate");
            return false;
        } else if (!(memberRequest.getGender().equalsIgnoreCase("Perempuan") || memberRequest.getGender().equalsIgnoreCase("Laki - Laki"))){
            response.setMessage("Only 2 Gender");
            return false;
        }
        else if (existingNumber.isPresent()) {
            response.setMessage("Phone Number is already exists");
            return false;
        }

        int code  = (int) (memberRepo.count() + 5);
        String codeNumber = "00"+code;

        Member member = new Member(null, memberRequest.getName().trim(),codeNumber, memberRequest.getPhoneNumber().trim(),memberRequest.getGender());
        memberRepo.save(member);
        response.setData(new DtoMemberResponse(member.getCodeMember(), member.getName(),  member.getGender(), member.getPhoneNumber()));
        return true;
    }

    public Page<DtoMemberResponse> pageView(int page, int limit){
        Pageable pageable = PageRequest.of(page, limit);
        Page<Member> result =  memberRepo.findAllByIsDeletedIsFalseOrderByNameAsc(pageable);
        List<DtoMemberResponse> listMember = new ArrayList<>();
        for (Member members: result.getContent()){
            DtoMemberResponse memberResponse = new DtoMemberResponse(members.getCodeMember(),members.getName(),
                    members.getGender(), members.getPhoneNumber());
            listMember.add(memberResponse);
        }
        return new PageImpl(listMember, PageRequest.of(page, limit), result.getTotalPages());
    }

    public Boolean updateMember(String code, DtoMemberRequest memberRequest, Response response){
        Optional<Member> existingMember = memberRepo.findByCodeMemberAndIsDeletedIsFalse(code);

        if (!existingMember.isPresent()){
            response.setMessage("Member Not Found");
            return false;
        } else if (!isPhoneNumberValid(memberRequest.getPhoneNumber())){
            response.setMessage("Format Number appropriate");
            return false;
        }

        if (existingMember.get().getName().equalsIgnoreCase(memberRequest.getName()) &&
                existingMember.get().getPhoneNumber().equalsIgnoreCase(memberRequest.getPhoneNumber())){
            response.setMessage("Data No Changes");
        }else {
            Optional<Member> existingNumber = memberRepo.findByPhoneNumberAndIsDeletedIsFalse(memberRequest.getPhoneNumber());
            if (existingNumber.isPresent()) {
                response.setMessage("Phone Number is already exists");
                return false;
            }
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

        List<Transaction> existingBorrowBook = transactionRepo.findByMemberIdAndIsPenaltyIsNull(existingMember.get().getId());

        if (!existingBorrowBook.isEmpty()){
            response.setMessage("The member is still borrowing books");
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
