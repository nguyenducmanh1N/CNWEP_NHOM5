package com.example.cnweb_nhom5.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.laptopshop.domain.Voucher;
import vn.hoidanit.laptopshop.repository.VoucherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }
