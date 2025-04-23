package org.mps;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mps.mutation.GaussianMutation;

import static org.junit.jupiter.api.Assertions.*;

public class GaussianMutationTest {


    @Test
    @DisplayName("mutate - Lanza excepción si el individuo es null")
    void testMutate_NullIndividual_ThrowsException() {
        // Arrange
        GaussianMutation mutator = new GaussianMutation(0.1, 1.0); // Tasas irrelevantes aquí
        int[] nullIndividual = null;
        String expectedErrorMessage = "No se puede realizar la mutación";

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            () -> mutator.mutate(nullIndividual),
            "Debería lanzar excepción si el individuo es null."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }

    @Test
    @DisplayName("mutate - Lanza excepción si el individuo está vacío")
    void testMutate_EmptyIndividual_ThrowsException() {
        // Arrange
        GaussianMutation mutator = new GaussianMutation(0.1, 1.0); // Tasas irrelevantes aquí
        int[] emptyIndividual = {};
        String expectedErrorMessage = "No se puede realizar la mutación";

        // Act & Assert
        EvolutionaryAlgorithmException thrownException = assertThrows(
            EvolutionaryAlgorithmException.class,
            () -> mutator.mutate(emptyIndividual),
            "Debería lanzar excepción si el individuo está vacío."
        );
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }

    @Test
    @DisplayName("mutate - No muta si mutationRate es 0")
    void testMutate_ZeroMutationRate_NoChange() throws EvolutionaryAlgorithmException {
        // Arrange
        double zeroMutationRate = 0.0;
        double stdDev = 1.0; // Desviación estándar (irrelevante si no hay mutación)
        GaussianMutation mutator = new GaussianMutation(zeroMutationRate, stdDev);
        int[] individual = {10, 20, 30, 40};
        int[] originalIndividual = individual.clone();

        // Act
        int[] mutatedIndividual = mutator.mutate(individual);

        // Assert: Verifica que el individuo no cambió (cubre la rama 'false' del if interno)
        assertNotNull(mutatedIndividual);
        assertArrayEquals(originalIndividual, mutatedIndividual,
                          "El individuo no debería cambiar si mutationRate es 0.");
        // También verifica que se devuelve una copia (por el .clone())
        assertNotSame(individual, mutatedIndividual, "Debería devolver una copia, no la misma instancia.");
    }

    @Test
    @DisplayName("mutate - Muta (posiblemente) si mutationRate es 1")
    void testMutate_FullMutationRate_ExecutesMutationLogic() throws EvolutionaryAlgorithmException {
        // Arrange
        double fullMutationRate = 1.0;
        double stdDev = 5.0;
        GaussianMutation mutator = new GaussianMutation(fullMutationRate, stdDev);
        int[] individual = {100, 200, 300};
        int[] originalIndividual = individual.clone(); // Copia para referencia

        // Act
        int[] mutatedIndividual = mutator.mutate(individual);

        // Assert: Verifica que la lógica de mutación se ejecutó (cubre la rama 'true' del if interno)
        assertNotNull(mutatedIndividual);
        assertEquals(originalIndividual.length, mutatedIndividual.length, "La longitud no debe cambiar.");
        assertNotSame(individual, mutatedIndividual, "Debería devolver una copia.");
    }

    @Test
    @DisplayName("Constructor por defecto no lanza excepción")
    void testDefaultConstructor_DoesNotThrowException() {
        // Arrange & Act & Assert
        assertDoesNotThrow(
            () -> new GaussianMutation(),
            "El constructor por defecto no debería lanzar excepciones."
        );
        //Para probar el constructor
    }

    @Test
    @DisplayName("Constructor parametrizado no lanza excepción")
    void testParameterizedConstructor_DoesNotThrowException() {

        double mutationRate = 0.05;
        double standardDeviation = 2.0;


        assertDoesNotThrow(
            () -> new GaussianMutation(mutationRate, standardDeviation),
            "El constructor parametrizado no debería lanzar excepciones con valores válidos."
        );

    }
}