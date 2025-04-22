package org.mps;

import org.mps.crossover.CrossoverOperator;
import org.mps.mutation.MutationOperator;
import org.mps.selection.SelectionOperator;

/**
 * La clase EvolutionaryAlgorithm representa un algoritmo evolutivo básico que
 * se utiliza para resolver problemas de optimización.
 * Este algoritmo se basa en el proceso de evolución biológica y sigue una serie
 * de pasos para mejorar progresivamente una población de soluciones candidatas.
 *
 * El proceso de optimización se realiza en varias etapas:
 * 1. Selección: Se seleccionan las soluciones más aptas para ser utilizadas
 * como progenitores en la próxima generación. Esto se realiza mediante
 * operadores de selección como la selección de torneo, etc.
 * 2. Cruce: Se aplican operadores de cruce a los progenitores seleccionados
 * para generar una nueva población de descendientes. Esto implica la
 * combinación de características de dos o más soluciones candidatas para
 * producir nuevas soluciones.
 * 3. Mutación: Ocasionalmente, se aplican operadores de mutación a los
 * descendientes generados para introducir variabilidad en la población y evitar
 * la convergencia prematura hacia un óptimo local.
 * 4. Reemplazo: Los descendientes reemplazan a una parte de la población
 * anterior.
 *
 * La clase EvolutionaryAlgorithm proporciona una implementación básica de este
 * proceso de optimización, permitiendo la personalización mediante el uso de
 * diferentes operadores de selección, cruce y mutación.
 */
public class EvolutionaryAlgorithm {
    private SelectionOperator selectionOperator;
    private MutationOperator mutationOperator;
    private CrossoverOperator crossoverOperator;

    public EvolutionaryAlgorithm(SelectionOperator selectionOperator, MutationOperator mutationOperator,
            CrossoverOperator crossoverOperator) throws EvolutionaryAlgorithmException {
        if (selectionOperator == null || mutationOperator == null || crossoverOperator == null) {
            throw new EvolutionaryAlgorithmException("Argumentos nulos");
        }
        this.selectionOperator = selectionOperator;
        this.mutationOperator = mutationOperator;
        this.crossoverOperator = crossoverOperator;
    }

    

    public int[][] optimize(int[][] population) throws EvolutionaryAlgorithmException {

        if (population == null || population.length == 0) {
            throw new EvolutionaryAlgorithmException("Poblacion no valida");
        }
        if (population[0] == null) {
             throw new EvolutionaryAlgorithmException("El primer individuo de la población es nulo.");
        }
        if (population.length < 2) {
            throw new EvolutionaryAlgorithmException("Poblacion menor a 2 personas");
        }
        if (population.length % 2 != 0) {
            throw new EvolutionaryAlgorithmException("La población debe tener un tamaño par para aplicar cruces por parejas.");
        }

        int[][] offspringPopulation = new int[population.length][population[0].length];
    
        // Aplicamos operadores de selección y cruce para generar descendientes
        for (int i = 0; i < population.length; i += 2) { // Bucle seguro por validación de tamaño par
    
            int[] parent1 = selectionOperator.select(population[i]);
            int[] parent2 = selectionOperator.select(population[i + 1]);
    
            // Validación de padres nulos añadida
            if (parent1 == null || parent2 == null) {
                throw new EvolutionaryAlgorithmException("Padre seleccionado es nulo.");
            }
    
            // Aplicamos el operador de cruce para generar dos descendientes
            int[][] offspring = crossoverOperator.crossover(parent1, parent2);
    
            // Validación de resultado de cruce añadida
            if (offspring == null || offspring.length != 2 || offspring[0] == null || offspring[1] == null) {
                throw new EvolutionaryAlgorithmException("Resultado de cruce inválido.");
            }
    
            offspringPopulation[i] = offspring[0];
            offspringPopulation[i + 1] = offspring[1];
        }
    
        // Aplicamos operador de mutación a los descendientes
        for (int i = 0; i < offspringPopulation.length; i++) {
            int[] mutatedOffspring = mutationOperator.mutate(offspringPopulation[i]); // Guardar resultado temporalmente
            // Validación de resultado de mutación añadida
            if (mutatedOffspring == null) {
                throw new EvolutionaryAlgorithmException("Resultado de mutación nulo.");
            }
            offspringPopulation[i] = mutatedOffspring; // Asignar resultado validado
        }
    
        // Reemplazo utilizando el método 'better' existente
        for (int i = 0; i < population.length; i++) {
            // Llama directamente a better para comparar el descendiente final con el padre original
            // Se asume que better maneja internamente (o devuelve false) si los individuos son nulos o de longitudes diferentes
            if (better(offspringPopulation[i], population[i])) {
                population[i] = offspringPopulation[i];
            }
        }
    
        return population;
    }

    /**
 * Compara dos individuos según la suma de sus genes.
 * Retorna true si el primer individuo tiene mejor fitness (menor suma).
 */
public boolean better(int[] individual1, int[] individual2) {
    if (individual1 == null || individual2 == null || individual1.length != individual2.length) {
        return false;
    }

    int sum1 = 0, sum2 = 0;
    for (int i = 0; i < individual1.length; i++) {
        sum1 += individual1[i];
        sum2 += individual2[i];
    }
    return sum1 < sum2;
}

    

}
