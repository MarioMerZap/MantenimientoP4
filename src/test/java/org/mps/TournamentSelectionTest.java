package org.mps;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.selection.TournamentSelection;

public class TournamentSelectionTest {
      
      @Test
      @DisplayName("Constructor válido con tamaño de torneo mayor que 0")
      public void testConstructor_Tamanyo_Torneo_Valido() throws EvolutionaryAlgorithmException {
            int tournamentSize = 3;
            TournamentSelection tournamentSelection = new TournamentSelection(tournamentSize);

            assertDoesNotThrow(() -> new TournamentSelection(tournamentSize));
      }

      @Test
      @DisplayName("Constructor con tamaño de torneo no válido lanza excepción")
      public void testConstructor_Tamanyio_Torneo_No_Valido_LanzaExcepcion() {
            int invalidTournamentSize = 0;

            assertThrows(EvolutionaryAlgorithmException.class, () -> new TournamentSelection(invalidTournamentSize));
      }

      @Test
    @DisplayName("Selección válida con población de tamaño mayor que el tamaño del torneo")
    public void select_poblacion_valida() throws EvolutionaryAlgorithmException {
        int tournamentSize = 3;
        TournamentSelection tournamentSelection = new TournamentSelection(tournamentSize);
        int[] population = {5, 7, 3, 9, 6};
        
        int[] selected = tournamentSelection.select(population);
        
        assertEquals(population.length, selected.length);
    }

    @Test
    @DisplayName("Selección con población nula lanza excepción")
    public void select_Poblacion_Nula_LanzaExcepcion() throws EvolutionaryAlgorithmException {
        int tournamentSize = 3;
        TournamentSelection tournamentSelection = new TournamentSelection(tournamentSize);
        
        assertThrows(EvolutionaryAlgorithmException.class, () -> tournamentSelection.select(null));
    }

    @Test
    @DisplayName("Selección con población más pequeña que el tamaño del torneo lanza excepción")
    public void select_Poblacion_Pequenya_LanzaExcepcion() throws EvolutionaryAlgorithmException {
        int tournamentSize = 5; 
        TournamentSelection tournamentSelection = new TournamentSelection(tournamentSize);
        int[] population = {5, 7, 3};
        
        assertThrows(EvolutionaryAlgorithmException.class, () -> tournamentSelection.select(population));
    }

    @Test
    @DisplayName("Selección con población vacía lanza excepción")
    public void select_Poblacion_Vacia_LanzaExcepcion() throws EvolutionaryAlgorithmException {
        int tournamentSize = 3;
        TournamentSelection tournamentSelection = new TournamentSelection(tournamentSize);
        
        int[] emptyPopulation = {};
        
        assertThrows(EvolutionaryAlgorithmException.class, () -> tournamentSelection.select(emptyPopulation));
    }

}