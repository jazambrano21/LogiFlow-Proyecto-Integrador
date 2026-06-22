'use strict';

/**
 * Tag template literal para syntax highlighting de GraphQL en editores.
 * No hace ninguna transformación — devuelve el string tal cual.
 */
function gql(strings, ...values) {
  let result = '';
  strings.forEach((str, i) => {
    result += str;
    if (i < values.length) result += values[i];
  });
  return result;
}

module.exports = { gql };
