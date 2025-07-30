package com.example.cadastroaluno.src;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    public Curso(String curso, String professor, String periodo) {
        this.curso = curso;
        this.professor = professor;
        this.periodo = periodo;
    }

    String curso, professor, periodo;
    static List<Curso> listaCurso = new ArrayList<>();

    public static void criarCurso() {

        Curso curso1 = new Curso("Matematica", "Augusto Barros", "8 periodos");
        Curso curso2 = new Curso("Letras", "Maria do Carmo", "8 periodos");
        Curso curso3 = new Curso("Historia", "Fernando Rocha", "6 periodos");
        Curso curso4 = new Curso("Engenharia de Software", "Bruno Ferreira", "8 periodos");
        Curso curso5 = new Curso("Direito", "Fabiano Miranda", "4 periodos");

        listaCurso.add(curso1);
        listaCurso.add(curso2);
        listaCurso.add(curso3);
        listaCurso.add(curso4);
        listaCurso.add(curso5);
    }

    public String getcurso(){
        return curso;
    }
    public String getProfessor(){
        return professor;
    }
    public String getPeriodo(){
        return periodo;
    }
    public static List<Curso> getListaCurso(){
        return listaCurso;
    }

}

