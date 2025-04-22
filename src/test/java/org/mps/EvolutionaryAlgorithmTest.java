package org.mps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mps.crossover.CrossoverOperator;
import org.mps.mutation.MutationOperator;
import org.mps.selection.SelectionOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EvolutionaryAlgorithmTest {
    

    private EvolutionaryAlgorithm algorithm;
    private SelectionOperator selectionOperator;
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;

    @BeforeEach
    public void setUp() throws EvolutionaryAlgorithmException {
        selectionOperator = mock(SelectionOperator.class);
        crossoverOperator = mock(CrossoverOperator.class);
        mutationOperator = mock(MutationOperator.class);
        algorithm = new EvolutionaryAlgorithm(selectionOperator, mutationOperator, crossoverOperator);
    }

    @Test
    public void testNullPopulation_throwsException() {
        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(null);
        });
        assertEquals("Poblacion no valida", ex.getMessage());
    }

    @Test
    public void testEmptyPopulation_throwsException() {
        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(new int[0][]);
        });
        assertEquals("Poblacion no valida", ex.getMessage());
    }
    
    @Test
    @DisplayName("Constructor - Lanza excepción si SelectionOperator es null (AAA)")
    void testConstructor_NullSelectionOperator_ThrowsException_AAA() {
        // Arrange
        SelectionOperator nullSelection = null;
        String expectedErrorMessage = "Argumentos nulos"; // Asegúrate que coincide con tu mensaje

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            // Pasamos null para selection, y los mocks válidos para los otros
            () -> new EvolutionaryAlgorithm(nullSelection, mutationOperator, crossoverOperator),
            "El constructor debería lanzar EvolutionaryAlgorithmException si SelectionOperator es null."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    @DisplayName("Constructor - Lanza excepción si MutationOperator es null (AAA)")
    void testConstructor_NullMutationOperator_ThrowsException_AAA() {
        // Arrange
        MutationOperator nullMutation = null;
        String expectedErrorMessage = "Argumentos nulos";

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
             // Pasamos null para mutation, y los mocks válidos para los otros
            () -> new EvolutionaryAlgorithm(selectionOperator, nullMutation, crossoverOperator),
            "El constructor debería lanzar EvolutionaryAlgorithmException si MutationOperator es null."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    @DisplayName("Constructor - Lanza excepción si CrossoverOperator es null (AAA)")
    void testConstructor_NullCrossoverOperator_ThrowsException_AAA() {
        // Arrange
        CrossoverOperator nullCrossover = null;
        String expectedErrorMessage = "Argumentos nulos";

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
             // Pasamos null para crossover, y los mocks válidos para los otros
            () -> new EvolutionaryAlgorithm(selectionOperator, mutationOperator, nullCrossover),
            "El constructor debería lanzar EvolutionaryAlgorithmException si CrossoverOperator es null."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }
    
    @Test
    public void testOddSizePopulation_throwsException() {
        int[][] population = {
            {1, 1, 1, 1},  // Individuo 0 - Array interno inicializado
            {2, 2, 2, 2},  // Individuo 1 - Array interno inicializado
            {3, 3, 3, 3}   // Individuo 2 - Array interno inicializado
                           // Tamaño total = 3 (impar)
    };
        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(population);
        });
        assertEquals("La población debe tener un tamaño par para aplicar cruces por parejas.", ex.getMessage());
    }

    @Test
    public void testValidFlow_executesWithoutError() throws EvolutionaryAlgorithmException {
        int[][] population = { {1, 2}, {3, 4} };

        when(selectionOperator.select(any())).thenReturn(new int[]{1, 2});
        when(crossoverOperator.crossover(any(), any())).thenReturn(new int[][]{{1, 1}, {2, 2}});
        when(mutationOperator.mutate(any())).thenReturn(new int[]{0, 0});

        int[][] result = algorithm.optimize(population);

        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    public void testNullParentFromSelect_throwsException() throws EvolutionaryAlgorithmException {
        int[][] population = { {1, 2}, {3, 4} };
        when(selectionOperator.select(population[0])).thenReturn(null);
        when(selectionOperator.select(population[1])).thenReturn(new int[]{3, 4});

        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(population);
        });

        assertEquals("Padre seleccionado es nulo.", ex.getMessage());
    }

    @Test
    public void testInvalidCrossover_throwsException() throws EvolutionaryAlgorithmException {
        int[][] population = { {1, 2}, {3, 4} };
        when(selectionOperator.select(any())).thenReturn(new int[]{1, 2});
        when(crossoverOperator.crossover(any(), any())).thenReturn(new int[1][2]);

        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(population);
        });

        assertEquals("Resultado de cruce inválido.", ex.getMessage());
    }
    @Test
    @DisplayName("Optimize con primer individuo nulo lanza excepción correcta (AAA)")
    void testOptimize_FirstIndividualIsNull_ThrowsCorrectException_AAA() {
        // Arrange: Crear una población donde el primer elemento es null,
        // pero la población tiene tamaño suficiente (>=1) para pasar la primera validación.
        // Se usa tamaño 2 para evitar que falle por la condición de tamaño < 2.
        int[][] populationWithNullFirst = { null, {1, 1} };
        String expectedErrorMessage = "El primer individuo de la población es nulo.";

        // Act: Ejecutar el método optimize y capturar la excepción lanzada.
        // El 'Act' real es la ejecución de 'algorithm.optimize(...)' dentro del lambda.
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            () -> algorithm.optimize(populationWithNullFirst),
            "Debería lanzar EvolutionaryAlgorithmException si el primer individuo es nulo."
        );
        assertNotNull(thrownException, "Se esperaba que se lanzara una excepción."); // Opcional, assertThrows ya lo verifica
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de la excepción no coincide con el esperado.");
    }

    @Test
    @DisplayName("Optimize con población de tamaño 1 lanza excepción correcta (AAA)")
    void testOptimize_PopulationSizeOne_ThrowsCorrectException_AAA() {
        // Arrange: Crear una población válida en su contenido (individuo no nulo)
        // pero con tamaño exactamente 1.
        int[][] populationSizeOne = { {1, 2, 3} }; // Individuo interno válido
        String expectedErrorMessage = "Poblacion menor a 2 personas";

 
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            () -> algorithm.optimize(populationSizeOne),
            "Debería lanzar EvolutionaryAlgorithmException si el tamaño de la población es 1."
        );

        assertNotNull(thrownException, "Se esperaba que se lanzara una excepción."); 
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de la excepción no coincide con el esperado.");
    }

    @Test
    @DisplayName("better - Devuelve false si el primer individuo es null (AAA)")
    void testBetter_FirstIndividualIsNull_ReturnsFalse_AAA() {
        // Arrange
        int[] individual1 = null;
        int[] individual2 = {1, 2}; // Segundo individuo válido

        boolean result = algorithm.better(individual1, individual2);

        assertFalse(result, "better debería devolver false cuando el primer individuo es null.");
    }

    @Test
    public void testNullMutation_throwsException() throws EvolutionaryAlgorithmException {
        int[][] population = { {1, 2}, {3, 4} };
        when(selectionOperator.select(any())).thenReturn(new int[]{1, 2});
        when(crossoverOperator.crossover(any(), any())).thenReturn(new int[][]{{1, 1}, {2, 2}});
        when(mutationOperator.mutate(any())).thenReturn(null);

        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(population);
        });

        assertEquals("Resultado de mutación nulo.", ex.getMessage());
    }
} 
