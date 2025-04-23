package org.mps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.crossover.TwoPointCrossover;

public class TwoPointCrossoverTest {
      
      @Test
      @DisplayName("Test de cruce de dos puntos")
      public void testCrossover() throws EvolutionaryAlgorithmException {
            
            int[] padre1 = {1, 2, 3, 4, 5};
            int[] padre2 = {6, 7, 8, 9, 10};

            TwoPointCrossover crossover = new TwoPointCrossover();

            int[][] offspring;
            offspring = crossover.crossover(padre1, padre2);

            
            assertEquals(2, offspring.length);
            assertEquals(padre1.length, offspring[0].length);
            assertEquals(padre2.length, offspring[1].length);
      }

      @Test
      void crossover_Padre_Nulo_LanzaExcepcion() {
            TwoPointCrossover crossover = new TwoPointCrossover();
            
            int[] padre1 = null;
            int[] padre2 = { 1, 2, 3, 4, 5 };

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                  crossover.crossover(padre1, padre2);
            });
      }


      @Test
      @DisplayName("Lanza excepción si el segundo padre es nulo")
      void testCrossover_SecondParentIsNull_ThrowsException() {
   
          TwoPointCrossover crossover = new TwoPointCrossover();
          int[] parent1 = {1, 2, 3, 4, 5}; // Válido
          int[] parent2 = null;           // Null
          String expectedErrorMessage = "No se ha podido realizar el cruce";

          EvolutionaryAlgorithmException thrownException = assertThrows(
              EvolutionaryAlgorithmException.class,
              () -> crossover.crossover(parent1, parent2),
              "Debería lanzar excepción si el segundo padre es nulo."
          );
          assertEquals(expectedErrorMessage, thrownException.getMessage());
      }

      @Test
      @DisplayName("Lanza excepción si longitud de padres es 1")
      void testCrossover_ParentLengthIsOne_ThrowsException() {

          TwoPointCrossover crossover = new TwoPointCrossover();
          int[] parent1Len1 = {1};
          int[] parent2Len1 = {2}; // Longitud también 1
          String expectedErrorMessage = "No se ha podido realizar el cruce";

          EvolutionaryAlgorithmException thrownException = assertThrows(
              EvolutionaryAlgorithmException.class,
              () -> crossover.crossover(parent1Len1, parent2Len1),
              "Debería lanzar excepción si longitud es 1."
          );
          assertEquals(expectedErrorMessage, thrownException.getMessage());
      }
 
}