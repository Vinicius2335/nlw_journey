package com.github.vinicius2335.planner.utils;

import com.github.javafaker.Faker;

import java.util.Locale;

public interface FakerCreator {
    Faker FAKER = new Faker(new Locale("pt-BR"));
}
