package com.example.cnweb_nhom5.controller.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.cnweb_nhom5.domain.Factory;
import com.example.cnweb_nhom5.service.FactoryService;

import jakarta.validation.Valid;
