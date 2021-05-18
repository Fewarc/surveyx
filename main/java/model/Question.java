/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Piotr Berezka
 */
public class Question {
    
    int position;
    String content;
    questionType type;
    boolean answerNeccesary;
    
    public enum questionType{RADIO_BUTTON, CHECK_BOX, YES_NO, TEXT}
    
}
