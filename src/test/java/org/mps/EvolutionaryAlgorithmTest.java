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
    @DisplayName("Constructor - Lanza excepción si SelectionOperator es null")
    void testConstructor_NullSelectionOperator_ThrowsException() {
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
    @DisplayName("Constructor - Lanza excepción si MutationOperator es null")
    void testConstructor_NullMutationOperator_ThrowsException() {
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
    @DisplayName("Optimize - Lanza excepción si la selección devuelve un padre nulo")
    void testOptimize_NullParentFromSelection_ThrowsException() throws EvolutionaryAlgorithmException {
        
        int[][] population = { {1, 1}, {2, 2} };
        int[] validParent = {2, 2};
        String expectedErrorMessage = "Padre seleccionado es nulo.";
    
            // Mocks: devuelven null para el primer padre, válido para el segundo

            when(selectionOperator.select(population[0])).thenReturn(null);
            when(selectionOperator.select(population[1])).thenReturn(validParent);
    
            EvolutionaryAlgorithmException thrownException = assertThrows(
                EvolutionaryAlgorithmException.class,
                () -> algorithm.optimize(population),
                "Debería lanzar excepción si selectionOperator devuelve null."
            );
            assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    
        }

    @Test
    @DisplayName("Optimize - Lanza excepción si el cruce devuelve null")
    void testOptimize_NullOffspringFromCrossover_ThrowsException() throws EvolutionaryAlgorithmException {
        int[][] population = { { 1, 1 }, { 2, 2 } };
        int[] parent1 = { 1, 1 };
        int[] parent2 = { 2, 2 };
        String expectedErrorMessage = "Resultado de cruce inválido.";

        // Configurar Mocks: select devuelve padres válidos, crossover devuelve null
        when(selectionOperator.select(population[0])).thenReturn(parent1);
        when(selectionOperator.select(population[1])).thenReturn(parent2);
        when(crossoverOperator.crossover(parent1, parent2)).thenReturn(null); // <- El caso a probar

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
                EvolutionaryAlgorithmException.class,
                () -> algorithm.optimize(population),
                "Debería lanzar excepción si crossover devuelve null.");
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");

    }
       
    @Test
    @DisplayName("Optimize - Lanza excepción si el cruce devuelve array de tamaño incorrecto")
    void testOptimize_WrongSizeOffspringFromCrossover_ThrowsException() throws EvolutionaryAlgorithmException {
        // Arrange: Población válida, padres válidos
        int[][] population = { { 1, 1 }, { 2, 2 } };
        int[] parent1 = { 1, 1 };
        int[] parent2 = { 2, 2 };
        int[][] wrongSizeOffspring = { { 3, 3 } }; // Solo 1 descendiente
        String expectedErrorMessage = "Resultado de cruce inválido.";

        // Configurar Mocks: select OK, crossover devuelve array de tamaño 1
        when(selectionOperator.select(population[0])).thenReturn(parent1);
        when(selectionOperator.select(population[1])).thenReturn(parent2);
        when(crossoverOperator.crossover(parent1, parent2)).thenReturn(wrongSizeOffspring); // <- El caso a probar

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
                EvolutionaryAlgorithmException.class,
                () -> algorithm.optimize(population),
                "Debería lanzar excepción si crossover devuelve un array de tamaño != 2.");
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }
    
        @Test
    @DisplayName("Optimize - Lanza excepción si el cruce devuelve primer descendiente nulo")
    void testOptimize_NullFirstOffspringFromCrossover_ThrowsException() throws EvolutionaryAlgorithmException {
        // Arrange: Población válida, padres válidos
        int[][] population = { { 1, 1 }, { 2, 2 } };
        int[] parent1 = { 1, 1 };
        int[] parent2 = { 2, 2 };
        int[][] offspringWithNull = { null, { 3, 3 } }; // Primer descendiente es null
        String expectedErrorMessage = "Resultado de cruce inválido.";

        // Configurar Mocks: select OK, crossover devuelve descendiente null
        when(selectionOperator.select(population[0])).thenReturn(parent1);
        when(selectionOperator.select(population[1])).thenReturn(parent2);
        when(crossoverOperator.crossover(parent1, parent2)).thenReturn(offspringWithNull); // <- El caso a probar

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
                EvolutionaryAlgorithmException.class,
                () -> algorithm.optimize(population),
                "Debería lanzar excepción si crossover devuelve un primer descendiente nulo.");
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }
    
    @Test
    @DisplayName("Optimize - Lanza excepción si el cruce devuelve segundo descendiente nulo")
    void testOptimize_NullSecondOffspringFromCrossover_ThrowsException() throws EvolutionaryAlgorithmException {
        // Arrange: Población válida, padres válidos
        int[][] population = { { 1, 1 }, { 2, 2 } };
        int[] parent1 = { 1, 1 };
        int[] parent2 = { 2, 2 };
        int[][] offspringWithNull = { { 3, 3 }, null }; 
        String expectedErrorMessage = "Resultado de cruce inválido.";

        // Configurar Mocks: select OK, crossover devuelve descendiente null
        when(selectionOperator.select(population[0])).thenReturn(parent1);
        when(selectionOperator.select(population[1])).thenReturn(parent2);
        when(crossoverOperator.crossover(parent1, parent2)).thenReturn(offspringWithNull); // <- El caso a probar

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
                EvolutionaryAlgorithmException.class,
                () -> algorithm.optimize(population),
                "Debería lanzar excepción si crossover devuelve un segundo descendiente nulo.");
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    @DisplayName("Optimize - No reemplaza si el descendiente no es mejor")
    void testOptimize_NoReplacementWhenOffspringNotBetter() throws EvolutionaryAlgorithmException {
        // Arrange: Población inicial
        int[][] population = { {1, 2}, {8, 8} }; // Sumas: 3, 16
        // Guardar copia para comparar al final
        int[][] originalPopulation = new int[population.length][];
        for(int i=0; i<population.length; i++) {
            originalPopulation[i] = population[i].clone();
        }

        // Padres seleccionados (pueden ser los originales o no)
        int[] parent1 = {1, 2};
        int[] parent2 = {8, 8};

        // Descendientes del cruce
        int[][] offspringAfterCrossover = { {5, 5}, {4, 4} }; // Sumas: 10, 8

        // Descendientes después de mutación (configurados para NO ser mejores)
        int[] mutatedOffspring1 = {5, 5}; // Suma 10 >= Suma padre1 (3) -> NO reemplazar
        int[] mutatedOffspring2 = {9, 9}; // Suma 18 >= Suma padre2 (16) -> NO reemplazar

        when(selectionOperator.select(population[0])).thenReturn(parent1);
        when(selectionOperator.select(population[1])).thenReturn(parent2);
        when(crossoverOperator.crossover(parent1, parent2)).thenReturn(offspringAfterCrossover);
        when(mutationOperator.mutate(offspringAfterCrossover[0])).thenReturn(mutatedOffspring1);
        when(mutationOperator.mutate(offspringAfterCrossover[1])).thenReturn(mutatedOffspring2);

        int[][] resultPopulation = algorithm.optimize(population);

        assertArrayEquals(originalPopulation[0], resultPopulation[0], "El primer individuo no debió ser reemplazado.");
        assertArrayEquals(originalPopulation[1], resultPopulation[1], "El segundo individuo no debió ser reemplazado.");
    }

    @Test
    @DisplayName("Optimize - Lanza excepción si el SEGUNDO padre seleccionado es nulo")
    void testOptimize_NullSecondParentFromSelection_ThrowsException() throws EvolutionaryAlgorithmException {
        // Arrange: Población inicial válida
        int[][] population = { {10, 10}, {20, 20} };
        int[] validParent1 = {10, 10}; // El primer padre es válido
        String expectedErrorMessage = "Padre seleccionado es nulo.";

        // Configurar Mocks: Primer select devuelve padre válido, SEGUNDO select devuelve null
        when(selectionOperator.select(population[0])).thenReturn(validParent1);
        when(selectionOperator.select(population[1])).thenReturn(null); // <- La condición a probar

        // Act & Assert: Ejecutar optimize y verificar la excepción y mensaje
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            () -> algorithm.optimize(population),
            "Debería lanzar excepción si el segundo padre seleccionado es null."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage(), "El mensaje de error no coincide.");
    }

    @Test
    @DisplayName("Constructor - Lanza excepción si CrossoverOperator es null")
    void testConstructor_NullCrossoverOperator_ThrowsException() {
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
    @DisplayName("Optimize con primer individuo nulo lanza excepción correcta")
    void testOptimize_FirstIndividualIsNull_ThrowsCorrectException() {
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
    @DisplayName("Optimize con población de tamaño 1 lanza excepción correcta")
    void testOptimize_PopulationSizeOne_ThrowsCorrectException() {
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
    @DisplayName("better - Devuelve false si el primer individuo es null")
    void testBetter_FirstIndividualIsNull_ReturnsFalse() {
        // Arrange
        int[] individual1 = null;
        int[] individual2 = {1, 2};

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert
        assertFalse(result, "better debería devolver false cuando el primer individuo es null.");
    }

    @Test
    @DisplayName("better - Devuelve false si el segundo individuo es null")
    void testBetter_SecondIndividualIsNull_ReturnsFalse() {
        // Arrange
        int[] individual1 = {1, 2};
        int[] individual2 = null;

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert
        assertFalse(result, "better debería devolver false cuando el segundo individuo es null.");
    }

    @Test
    @DisplayName("better - Devuelve false si ambos individuos son null")
    void testBetter_BothIndividualsAreNull_ReturnsFalse() {
        // Arrange
        int[] individual1 = null;
        int[] individual2 = null;

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert
        assertFalse(result, "better debería devolver false cuando ambos individuos son null.");
    }

    @Test
    @DisplayName("better - Devuelve false si los individuos tienen longitudes diferentes")
    void testBetter_DifferentLengths_ReturnsFalse() {
        // Arrange
        int[] individual1 = {1, 2, 3}; // Longitud 3
        int[] individual2 = {4, 5};    // Longitud 2

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert
        assertFalse(result, "better debería devolver false cuando los individuos tienen longitudes diferentes.");
    }

    // Casos para la segunda condición (comparación de sumas) -> requiere que la primera condición sea false

    @Test
    @DisplayName("better - Devuelve true si sum1 < sum2")
    void testBetter_Sum1LessThanSum2_ReturnsTrue() {
        // Arrange: Inputs válidos (no null, misma longitud), sum1 < sum2
        int[] individual1 = {1, 2}; // Suma 3
        int[] individual2 = {5, 5}; // Suma 10

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert: Cubre la rama 'true' del return
        assertTrue(result, "better debería devolver true cuando la suma del primero es menor.");
    }

    @Test
    @DisplayName("better - Devuelve false si sum1 > sum2")
    void testBetter_Sum1GreaterThanSum2_ReturnsFalse() {
        // Arrange: Inputs válidos (no null, misma longitud), sum1 > sum2
        int[] individual1 = {10, 10}; // Suma 20
        int[] individual2 = {5, 5};   // Suma 10

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert: Cubre la rama 'false' del return
        assertFalse(result, "better debería devolver false cuando la suma del primero es mayor.");
    }

    @Test
    @DisplayName("better - Devuelve false si sum1 == sum2")
    void testBetter_Sum1EqualsSum2_ReturnsFalse() {
        // Arrange: Inputs válidos (no null, misma longitud), sum1 == sum2
        int[] individual1 = {6, 4}; // Suma 10
        int[] individual2 = {5, 5}; // Suma 10

        // Act
        boolean result = algorithm.better(individual1, individual2);

        // Assert: Cubre la rama 'false' del return
        assertFalse(result, "better debería devolver false cuando las sumas son iguales.");
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
