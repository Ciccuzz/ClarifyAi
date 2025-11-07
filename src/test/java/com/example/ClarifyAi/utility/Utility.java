package com.example.ClarifyAi.utility;

import com.example.ClarifyAi.dto.PromptRequest;
import com.example.ClarifyAi.model.ActionEnum;
import com.example.ClarifyAi.model.Length;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utility {

    public static final String VALID_TEXT = "La fotosintesi clorofilliana è un processo chimico per mezzo del quale le piante verdi e altri organismi producono sostanze organiche – principalmente carboidrati – a partire dal primo reagente, l'anidride carbonica atmosferica e l'acqua metabolica, in presenza di luce solare, rientrando tra i processi di anabolismo dei carboidrati, del tutto opposta ai processi inversi di catabolismo. Durante la fotosintesi, con la mediazione della clorofilla, la luce solare o artificiale permette di convertire sei molecole di CO2 e sei molecole d'H2O in una molecola di glucosio (C6H12O6), zucchero fondamentale per la vita della pianta. Come sottoprodotto della reazione si producono sei molecole di ossigeno, che la pianta libera nell'atmosfera attraverso gli stomi che si trovano nella foglia. La formula stechiometrica della reazione è: 6CO2 + 6H2O + luce → C6H12O6 + 6O2. Si tratta del processo di produzione primario di composti organici del carbonio da sostanze inorganiche nettamente dominante sulla Terra (trasforma circa 115×10^9 tonnellate di carbonio atmosferico in biomassa ogni anno), rientrando dunque nel cosiddetto ciclo del carbonio, ed è inoltre l'unico processo biologicamente importante in grado di raccogliere l'energia solare, da cui, fondamentalmente, dipende la vita sulla Terra (la quantità di energia solare catturata dalla fotosintesi è immensa, dell'ordine dei 100 terawatt, circa sei volte quanto consuma attualmente la civiltà umana).";
    public static final String NOT_VALID_TEXT = "Word ".repeat(1100);
    public static final String EMPTY_TEXT = "";

    public static final PromptRequest MAX_WORDS_REQUEST = new PromptRequest(VALID_TEXT, "summary", Length.PERSONALIZED, 100);
    public static final PromptRequest LENGTH_REQUEST = new PromptRequest(VALID_TEXT, "simplify", Length.SHORT, null);
    public static final PromptRequest EMPTY_TEXT_REQUEST = new PromptRequest(EMPTY_TEXT, "summary", Length.PERSONALIZED, 100);
    public static final PromptRequest NULL_TEXT_REQUEST = new PromptRequest(null, "summary", Length.PERSONALIZED, 100);


    public static final PromptRequest VALID_SUMMARY_REQUEST = new PromptRequest(VALID_TEXT, "summary", Length.SHORT, 80);
    public static final PromptRequest VALID_TRANSLATE_IT_REQUEST = new PromptRequest(VALID_TEXT, "translate-it", Length.PERSONALIZED, 80);
    public static final PromptRequest VALID_TRANSLATE_EN_REQUEST = new PromptRequest(VALID_TEXT, "translate-en", Length.PERSONALIZED, 80);
    public static final PromptRequest NULL_ACTION_REQUEST = new PromptRequest(VALID_TEXT, null, Length.PERSONALIZED, 80);

    public static final PromptRequest TOO_LONG_REQUEST = new PromptRequest(NOT_VALID_TEXT, "summary", Length.SHORT, 80);
    public static final PromptRequest VALID_TRANSLATION_REQUEST = new PromptRequest(VALID_TEXT, "translate-en", Length.TRANSLATION, null);
}