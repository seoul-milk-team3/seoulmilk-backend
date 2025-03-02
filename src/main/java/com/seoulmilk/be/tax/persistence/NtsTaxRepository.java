package com.seoulmilk.be.tax.persistence;

import com.seoulmilk.be.tax.domain.NtsTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NtsTaxRepository extends JpaRepository<NtsTax, Long> {

}
