package com.example.bookstore.entity;

import com.example.bookstore.model.enums.BookType;
import com.example.bookstore.model.enums.VoucherType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String code;

    Integer value;

    @Enumerated(EnumType.STRING)
    VoucherType voucherType;

    LocalDateTime startDate;
    LocalDateTime endDate;
}
