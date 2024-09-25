package com.example.cnweb_nhom5.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Role;
import com.example.cnweb_nhom5.domain.User;
import com.example.cnweb_nhom5.domain.dto.RegisterDTO;
//import com.example.cnweb_nhom5.repository.OrderRepository;
//import com.example.cnweb_nhom5.repository.ProductRepository;
import com.example.cnweb_nhom5.repository.RoleRepository;
import com.example.cnweb_nhom5.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    //fix
    //private final ProductRepository productRepository;
    //private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository
            //,ProductRepository productRepository,
            //OrderRepository orderRepository
            ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        //this.productRepository = productRepository;
        //this.orderRepository = orderRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getAllUsers(Pageable page) {
        return this.userRepository.findAll(page);
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public User handleSaveUser(User user) {
        User eric = this.userRepository.save(user);
        System.out.println(eric);
        return eric;
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public void deleteAUser(long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public long countUsers() {
        return this.userRepository.count();
    }
    //fix
    // public long countProducts() {
    //     return this.productRepository.count();
    // }

    // public long countOrders() {
    //     return this.orderRepository.count();
    // }
}
