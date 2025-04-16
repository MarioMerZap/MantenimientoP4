package org.mps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mps.crossover.CrossoverOperator;
import org.mps.mutation.MutationOperator;
import org.mps.selection.SelectionOperator;

import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals("Población no válida: es nula o vacía.", ex.getMessage());
    }

    @Test
    public void testEmptyPopulation_throwsException() {
        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(new int[0][]);
        });
        assertEquals("Población no válida: es nula o vacía.", ex.getMessage());
    }

    @Test
    public void testOddSizePopulation_throwsException() {
        int[][] population = new int[3][4];
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

        assertEquals("Uno de los padres seleccionados es nulo.", ex.getMessage());
    }

    @Test
    public void testInvalidCrossover_throwsException() throws EvolutionaryAlgorithmException {
        int[][] population = { {1, 2}, {3, 4} };
        when(selectionOperator.select(any())).thenReturn(new int[]{1, 2});
        when(crossoverOperator.crossover(any(), any())).thenReturn(new int[1][2]);

        EvolutionaryAlgorithmException ex = assertThrows(EvolutionaryAlgorithmException.class, () -> {
            algorithm.optimize(population);
        });

        assertEquals("El operador de cruce no generó dos descendientes válidos.", ex.getMessage());
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

        assertEquals("El operador de mutación devolvió un individuo nulo.", ex.getMessage());
    }
} 
