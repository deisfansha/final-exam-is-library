package com.example.IsLibrary.repositories;

import com.example.IsLibrary.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findByPhoneNumberAndIsDeletedIsFalse(String phoneNumber);

    @Query("SELECT m FROM Member m ORDER BY m.id DESC")
    List<Member> findLast();
}
